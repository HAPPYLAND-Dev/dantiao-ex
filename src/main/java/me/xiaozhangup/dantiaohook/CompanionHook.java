package me.xiaozhangup.dantiaohook;

import com.valorin.api.event.arena.ArenaDrawFinishEvent;
import com.valorin.api.event.arena.ArenaFinishEvent;
import com.valorin.api.event.arena.ArenaStartEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public class CompanionHook implements Listener {

//    private static HashMap<UUID, String> comp = new HashMap<>();
//
//    @EventHandler
//    public static void onStrart(ArenaStartEvent e) {
//        if (CAPI.isSummoned(e.getPlayer1())) {
//            comp.put(e.getPlayer1().getUniqueId() , getActiveCompanionName(e.getPlayer1()));
//            PlayerData.instanceOf(e.getPlayer1()).removeCompanion();
//            PlayerData.instanceOf(e.getPlayer1()).setActiveCompanionName("NONE");
//        } else {
//            comp.put(e.getPlayer1().getUniqueId() , null);
//        }
//
//        if (CAPI.isSummoned(e.getPlayer2())) {
//            comp.put(e.getPlayer2().getUniqueId() , getActiveCompanionName(e.getPlayer2()));
//            PlayerData.instanceOf(e.getPlayer2()).removeCompanion();
//            PlayerData.instanceOf(e.getPlayer2()).setActiveCompanionName("NONE");
//        } else {
//            comp.put(e.getPlayer2().getUniqueId() , null);
//        }
//    }
//
//    @EventHandler
//    public static void onDrawFinish(ArenaDrawFinishEvent e) {
//        if (comp.get(e.getPlayer1().getUniqueId()) != null) {
//            PlayerData.instanceOf(e.getPlayer1()).setActiveCompanionName(comp.get(e.getPlayer1().getUniqueId()));
//        }
//        if (comp.get(e.getPlayer2().getUniqueId()) != null) {
//            PlayerData.instanceOf(e.getPlayer2()).setActiveCompanionName(comp.get(e.getPlayer2().getUniqueId()));
//        }
//        comp.remove(e.getPlayer1().getUniqueId());
//        comp.remove(e.getPlayer2().getUniqueId());
//    }
//
//    @EventHandler
//    public static void onFinish(ArenaFinishEvent e) {
//                if (comp.get(e.getWinner().getUniqueId()) != null) {
//            PlayerData.instanceOf(e.getWinner()).setActiveCompanionName(comp.get(e.getWinner().getUniqueId()));
//        }
//        if (comp.get(e.getLoser().getUniqueId()) != null) {
//            PlayerData.instanceOf(e.getLoser()).setActiveCompanionName(comp.get(e.getLoser().getUniqueId()));
//        }
//        comp.remove(e.getWinner().getUniqueId());
//        comp.remove(e.getWinner().getUniqueId());
//    }
//
//    //我不得不这么写一次,原本的没法调用
//    public static String getActiveCompanionName(Player var1) {
//        return PlayerData.instanceOf(var1).getActiveCompanionName();
//    }
}
