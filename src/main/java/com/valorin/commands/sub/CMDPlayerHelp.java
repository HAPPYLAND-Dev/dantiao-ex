package com.valorin.commands.sub;

import static com.valorin.configuration.languagefile.MessageSender.sm;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.valorin.commands.SubCommand;

public class CMDPlayerHelp extends SubCommand {

	public CMDPlayerHelp() {
		super("help", "h");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		sm("", player);
		sm("&3&lDan&b&l&oTiao &f&l>> &a玩家帮助", player, false);
		sm("&b/dt rank(r) &f- &a查看单挑排行榜", player, false);
		sm("&b/dt lobby(l) &f- &a传送至大厅", player, false);
		sm("&b/dt start(st) &f- &a打开匹配面板搜寻对手", player, false);
		sm("&b/dt timetable(tt) &f- &a查看匹配功能开放时间段", player, false);
		sm("&b/dt arenasinfo(ainfo) &f- &a查看所有竞技场信息", player, false);
		sm("&b/dt watch(w) <竞技场名称> &f- &a观战某个正在比赛的竞技场", player, false);
		sm("&b/dt send <玩家名> [竞技场名称(不填则随机)] &f- &a向某个玩家发送单挑请求", player, false);
		sm("&b/dt accept &f- &a接受某个玩家的单挑请求", player, false);
		sm("&b/dt deny &f- &a拒绝某个玩家的单挑请求", player, false);
		sm("&b/dt sendall(sa) &f- &a向全服发起单挑请求(需要特定邀请函)", player, false);
		sm("&b/dt quit(q) &f- &a认输(比赛时使用)", player, false);
		sm("&b/dt shop(s) &f- &a打开单挑积分商城", player, false);
		sm("&b/dt records(r) &f- &a打开单挑记录面板", player, false);
		sm("&b/dt points(p) me &f- &a查看我的单挑积分", player, false);
		sm("&b/dt changelang(cl) &f- &a查看所有语言文件", player, false);
		sm("&b/dt changelang(cl) <语言文件> &f- &e切换语言", player, false);
		sm("", player);
		return true;
	}

}
