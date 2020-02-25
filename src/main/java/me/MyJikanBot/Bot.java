package me.MyJikanBot;


import javax.security.auth.login.LoginException;

import me.MyJikanBot.Commands.Clear;
import me.MyJikanBot.Events.GuildMemberJoin;
import me.MyJikanBot.Events.GuildMemberLeave;
import me.MyJikanBot.Events.GuildMessageReactionAdd;
import me.MyJikanBot.Events.GuildMessageReceived;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;


public class Bot {
	public static JDA jda;
	public static String prefix = "!";
	
	// Main Method
	public static void main(String[] args) throws LoginException {
		jda = new JDABuilder(AccountType.BOT).setToken("Njc2NjAxMzQwOTk4NDUxMjIw.XlHDDQ.ViodIs8zm8DbFfgvq8dblX4EvQY" + "").build();
		jda.getPresence().setStatus(OnlineStatus.IDLE);
		jda.getPresence().setActivity(Activity.watching("20th century boys"));

		jda.addEventListener(new Command());
		jda.addEventListener(new Clear());
		jda.addEventListener(new GuildMemberJoin());
		jda.addEventListener(new GuildMemberLeave());
		jda.addEventListener(new GuildMessageReceived());
		jda.addEventListener(new GuildMessageReactionAdd());
	}
}
