package com.valorin.task;

import com.valorin.arenas.Arena;
import com.valorin.caches.*;
import com.valorin.configuration.ConfigManager;
import com.valorin.dan.CustomDan;
import com.valorin.dan.DanHandler;
import com.valorin.ranking.RankingData;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.valorin.Main.getInstance;
import static com.valorin.configuration.languagefile.MessageSender.*;
import static com.valorin.dan.ExpChange.changeExp;
import static com.valorin.util.SyncBroadcast.bc;

public class SettleEnd {
    public static void settle(Arena arena, Player w, Player l, boolean isDraw) {
        int time = arena.getTime();
        int startWay = arena.getStartWay();
        String winner = w.getName();
        String loser = l.getName();

        double player1Damage = arena.getDamage(true);
        double player1MaxDamage = arena.getMaxDamage(true);
        double player2Damage = arena.getDamage(false);
        double player2MaxDamage = arena.getMaxDamage(false);

        double winnerDamage, winnerMaxDamage, loserDamage, loserMaxDamage;
        if (arena.isp1(winner)) {
            winnerDamage = player1Damage;
            winnerMaxDamage = player1MaxDamage;
            loserDamage = player2Damage;
            loserMaxDamage = player2MaxDamage;
        } else {
            winnerDamage = player2Damage;
            winnerMaxDamage = player2MaxDamage;
            loserDamage = player1Damage;
            loserMaxDamage = player1MaxDamage;
        }

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = formatter.format(cal.getTime());
        ConfigManager configManager = getInstance().getConfigManager();
        DanCache danCache = getInstance().getCacheHandler().getDan();
        PointCache pointCache = getInstance().getCacheHandler().getPoint();
        RecordCache recordCache = getInstance().getCacheHandler().getRecord();

        String server = getInstance().getConfigManager().getServerName();
        int loserResult = 0, winnerResult = 0;

        double pointsAwarded = 0, pointsDeducted = 0;
        int winExpAwarded = 0, drawExpAwarded = 0, maxExpAwarded = 0;
        // ?????????
        if (startWay == 1) {
            pointsAwarded = configManager.getPointsAwardedByPanel();
            pointsDeducted = configManager.getPointsDeductedByPanel();
            winExpAwarded = configManager.getExpAwardedByPanel();
            drawExpAwarded = configManager.getDrawExpAwardedByPanel();
            maxExpAwarded = configManager.getMaxExpAwardedByPanel();
        }
        // ?????????
        if (startWay == 2) {
            pointsAwarded = configManager.getPointsAwardedByInviting();
            pointsDeducted = configManager.getPointsDeductedByInviting();
            winExpAwarded = configManager.getExpAwardedByInviting();
            drawExpAwarded = configManager.getDrawExpAwardedByInviting();
            maxExpAwarded = configManager.getMaxExpAwardedByInviting();
        }
        // ?????????
        if (startWay == 3) {
            pointsAwarded = configManager.getPointsAwardedByCompulsion();
            pointsDeducted = configManager.getPointsDeductedByCompulsion();
            winExpAwarded = configManager.getExpAwardedByCompulsion();
            drawExpAwarded = configManager.getDrawExpAwardedByCompulsion();
            maxExpAwarded = configManager.getMaxExpAwardedByCompulsion();
        }

        if (!isDraw) {
            loserResult = 1;
            winnerResult = 0;

            if (pointsAwarded != 0) {
                double now = pointCache.get(winner);
                pointCache.set(winner, now + pointsAwarded);
                sm("&b???????????????????????? &d{points} &b???????????????", w, "points",
                        new String[]{"" + pointsAwarded});
            }
            if (pointsDeducted != 0) {
                double now = pointCache.get(loser);
                double nowShowed;
                double pointsDeductedShowed = pointsDeducted;
                if (now > 0) {
                    if (now - pointsDeducted >= 0) {
                        pointCache.set(loser, now - pointsDeducted);
                        nowShowed = now - pointsDeducted;
                    } else {
                        pointCache.set(loser, 0);
                        nowShowed = 0;
                        pointsDeductedShowed = now;
                    }
                } else {
                    nowShowed = 0;
                    pointsDeductedShowed = 0;
                }
                sm("&7??????????????? &d{points} &7?????????????????????????????? &6{now} &7???", l, "points now",
                        new String[]{"" + pointsDeductedShowed, "" + nowShowed});
            }

            recordCache.addWins(winner);
            recordCache.addLoses(loser);
        } else {
            recordCache.addDraws(winner);
            recordCache.addDraws(loser);
        }

        ArenaInfoCache arenaInfoCache = getInstance().getCacheHandler()
                .getArenaInfo();
        String displayName = arenaInfoCache.get(arena.getName())
                .getDisplayName();
        String editName = arena.getName();
        int winnerExpChange = 0;
        int loserExpChange = 0;
        // ?????????????????????????????????????????????
        if (!isDraw) {
            RankingCache rankingCache = getInstance().getCacheHandler()
                    .getRanking();
            RankingData rankingData = getInstance().getRanking();

            int winnerRank = rankingData.getWinRank(winner);
            int loserRank = rankingData.getWinRank(loser);

            int winnerRank2 = rankingData.getKDRank(winner);
            int loserRank2 = rankingData.getKDRank(loser);

            int winnerOrder = 0;
            if (rankingCache.getWin() != null) {
                for (int i = 0; i < rankingCache.getWin().size(); i++) {
                    if (rankingCache.getWin().get(i).split("\\|")[0]
                            .equals(winner)) {
                        winnerOrder = i;
                    }
                }
            }
            int loserOrder = 0;
            if (rankingCache.getWin() != null) {
                for (int i = 0; i < rankingCache.getWin().size(); i++) {
                    if (rankingCache.getWin().get(i).split("\\|")[0]
                            .equals(loser)) {
                        loserOrder = i;
                    }
                }
            }

            if (winnerOrder <= loserOrder) {// winner???????????????loser
                // ????????????????????????????????????winner??????
                rankingData.rank(winner + "|" + recordCache.getWins(winner), true);
                rankingData.rank(loser + "|" + recordCache.getWins(loser), true);
            } else {
                rankingData.rank(loser + "|" + recordCache.getWins(loser), true);
                rankingData.rank(winner + "|" + recordCache.getWins(winner), true);
            }

            int winnerOrder2 = 0;
            if (rankingCache.getKD() != null) {
                for (int i = 0; i < rankingCache.getKD().size(); i++) {
                    if (rankingCache.getKD().get(i).split("\\|")[0]
                            .equals(winner)) {
                        winnerOrder2 = i;
                    }
                }
            }
            int loserOrder2 = 0;
            if (rankingCache.getKD() != null) {
                for (int i = 0; i < rankingCache.getKD().size(); i++) {
                    if (rankingCache.getKD().get(i).split("\\|")[0]
                            .equals(loser)) {
                        loserOrder2 = i;
                    }
                }
            }
            if (winnerOrder2 <= loserOrder2) {// winner???????????????loser
                // ????????????????????????????????????winner??????
                if (recordCache.getLoses(winner) != 0) {
                    rankingData.rank(
                            winner
                                    + "|"
                                    + ((double) recordCache.getWins(winner) / (double) recordCache
                                    .getLoses(winner)), false);
                } else {
                    rankingData.rank(
                            winner + "|"
                                    + ((double) recordCache.getWins(winner)),
                            false);
                }

                if (recordCache.getLoses(loser) != 0) {
                    rankingData.rank(
                            loser
                                    + "|"
                                    + ((double) recordCache.getWins(loser) / (double) recordCache
                                    .getLoses(loser)), false);
                } else {
                    rankingData.rank(
                            loser + "|" + ((double) recordCache.getWins(loser)),
                            false);
                }
            } else {
                if (recordCache.getLoses(loser) != 0) {
                    rankingData.rank(
                            loser
                                    + "|"
                                    + ((double) recordCache.getWins(loser) / (double) recordCache
                                    .getLoses(loser)), false);
                } else {
                    rankingData.rank(
                            loser + "|" + ((double) recordCache.getWins(loser)),
                            false);
                }
                if (recordCache.getLoses(winner) != 0) {
                    rankingData.rank(
                            winner
                                    + "|"
                                    + ((double) recordCache.getWins(winner) / (double) recordCache
                                    .getLoses(winner)), false);
                } else {
                    rankingData.rank(
                            winner + "|"
                                    + ((double) recordCache.getWins(winner)),
                            false);
                }
            }

            if (winnerRank != rankingData.getWinRank(winner)
                    && rankingData.getWinRank(winner) != 0) {
                sm("&b???????????????????????????&e{before}->{now}",
                        w,
                        "before now",
                        new String[]{"" + winnerRank,
                                "" + rankingData.getWinRank(winner)});
            }
            if (loserRank != rankingData.getWinRank(loser)
                    && rankingData.getWinRank(loser) != 0) {
                sm("&b???????????????????????????&e{before}->{now}",
                        l,
                        "before now",
                        new String[]{"" + loserRank,
                                "" + rankingData.getWinRank(loser)});
            }
            if (winnerRank2 != rankingData.getKDRank(winner)
                    && rankingData.getWinRank(winner) != 0) {
                sm("&bKD?????????????????????&e{before}->{now}",
                        w,
                        "before now",
                        new String[]{"" + winnerRank2,
                                "" + rankingData.getKDRank(winner)});
            }
            if (loserRank2 != rankingData.getKDRank(loser)
                    && rankingData.getWinRank(loser) != 0) {
                sm("&bKD?????????????????????&e{before}->{now}",
                        l,
                        "before now",
                        new String[]{"" + loserRank2,
                                "" + rankingData.getKDRank(loser)});
            }

            boolean b1 = arena.isp1(winner);
            int winnerExp;
            int loserExp;
            winnerExp = Math.min(arena.getExp(b1), maxExpAwarded);
            winnerExp = winnerExp + winExpAwarded;
            loserExp = winnerExp / 3;

            DanHandler dh = getInstance().getDanHandler();

            boolean protection;
            int protectionExp = configManager.getProtectionExp();
            int winnerExpNow = danCache.get(winner);
            int loserExpNow = danCache.get(loser);
            if (protectionExp == 0) { // ???????????????
                protection = false;
            } else {
                protection = winnerExpNow - loserExpNow >= protectionExp
                        || loserExpNow - winnerExpNow >= protectionExp;
            }

            int loserExpShow;
            int winnerExpShow;
            if (protection) {
                loserExpShow = 0;
                winnerExpShow = 0;
                sm("&c???????????????????????????????????????????????????", w, l);
            } else {
                changeExp(w, winnerExpNow + winnerExp);
                winnerExpChange = winnerExp;
                winnerExpShow = winnerExp;
                loserExpShow = loserExp;
                if (loserExpNow - loserExp > dh.getThreshold()) {
                    changeExp(l, loserExpNow - loserExp); // ??????????????????
                    loserExpChange = -loserExp;
                } else {
                    if (loserExpNow > dh.getThreshold()) {
                        loserExpShow = loserExpNow - dh.getThreshold();
                        changeExp(l, dh.getThreshold()); // ?????????????????????
                        loserExpChange = -(loserExpNow - dh.getThreshold());
                    } else {
                        loserExpShow = 0; // ????????????
                    }
                }
            }

            sml("&7============================================================| |                       &b???????????????|        &7??????????????????????????????????????????????????????????????????|                  &7??????????????? &a{exp} &7??????| |&7============================================================",
                    w, "exp", new String[]{"" + winnerExpShow});
            sml("&7============================================================| |                     &b???????????????|           &7????????????????????????????????????????????????|                &7??????????????? &c{exp} &7??????| |&7============================================================",
                    l, "exp", new String[]{"" + loserExpShow});
            if (displayName == null) {
                displayName = arena.getName();
            } else {
                displayName = displayName.replace("&", "??");
            }
            bc(gm("&b[??????]: &7?????? &e{winner} &7???????????????&r{arenaname}&r&7?????? &6{time}??? &7???????????? &e{loser}",
                    null, "winner arenaname time loser", new String[]{winner,
                            displayName, arena.getTime() + "", loser}),
                    startWay);

            dh.refreshPlayerDan(winner);
            dh.refreshPlayerDan(loser);
            if (arena.getDan(arena.isp1(winner)) != null) {
                CustomDan danBefore = arena.getDan(arena.isp1(winner));
                CustomDan danNow = dh.getDanByExp(winnerExpNow + winnerExpShow);
                if (!danBefore.equals(danNow)) {
                    bc(gm("&a[??????]: &7?????? &e{player} &7??????????????????????????????&r{dan}", null,
                            "player dan",
                            new String[]{winner, danNow.getDisplayName()}),
                            startWay);
                }
            } else {
                CustomDan danNow = dh.getDanByExp(winnerExpNow + winnerExpShow);
                if (danNow != null) {
                    bc(gm("&a[??????]: &7?????? &e{player} &7??????????????????????????????????????????????????????&r{dan}&7??????TA???????????????????????????????????????",
                            null, "player dan",
                            new String[]{winner, danNow.getDisplayName()}),
                            startWay);
                }
            }

            // ??????
            int winTime = recordCache.getWinningStreakTimes(winner);
            int maxWinTime = recordCache.getMaxWinningStreakTimes(winner);
            recordCache.addWinningStreakTimes(winner);
            if (winTime + 1 > maxWinTime) {
                recordCache.addMaxWinningStreakTimes(winner);
            }
            recordCache.clearWinningStreakTimes(loser);
            int reportTimes = configManager.getBroadcastWinningStreakTimes();
            if (reportTimes == 0) {
                reportTimes = 3;
            }
            if (winTime + 1 >= reportTimes) {
                bc(gm("&a[??????]: &7?????? &e{player} &7???????????????????????? &b{times} &7?????????",
                        null, "player times", new String[]{winner,
                                (winTime + 1) + ""}), startWay);
            }
        } else { // ??????
            changeExp(w, danCache.get(winner) + drawExpAwarded);
            changeExp(l, danCache.get(loser) + drawExpAwarded);
            winnerExpChange = drawExpAwarded;
            loserExpChange = winnerExpChange;
            sml("&7============================================================| |                    &b???????????????|          &7???????????????????????????????????????????????????|          &7??????????????? &a{exp} &7??????| |&7============================================================",
                    w, "exp", new String[]{"" + drawExpAwarded});
            sml("&7============================================================| |                    &b???????????????|          &7???????????????????????????????????????????????????|          &7??????????????? &a{exp} &7??????| |&7============================================================",
                    l, "exp", new String[]{"" + drawExpAwarded});
            if (displayName == null) {
                displayName = editName;
            } else {
                displayName = displayName.replace("&", "??");
            }
            bc(gm("&b[??????]: &7?????? &e{p1} &7??? &e{p2} &7???????????????&r{arenaname}&r&7?????????????????????????????????",
                    null, "p1 arenaname p2", new String[]{winner,
                            displayName, loser}), startWay);
        }

        getInstance().getHologramManager().setIsNeedRefresh(true);
        recordCache
                .add(loser, date, winner, server, time, loserDamage,
                        loserMaxDamage, loserResult, startWay, loserExpChange,
                        editName);
        recordCache.add(winner, date, loser, server, time, winnerDamage,
                winnerMaxDamage, winnerResult, startWay, winnerExpChange,
                editName);
        recordCache.refreshServerTotalGameTimes();
    }
}
