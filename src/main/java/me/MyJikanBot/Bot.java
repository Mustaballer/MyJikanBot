package me.MyJikanBot;

import javax.security.auth.login.LoginException;

import me.MyJikanBot.Commands.Clear;
import me.MyJikanBot.Commands.Kick;
import me.MyJikanBot.Commands.RandomAnime;
import me.MyJikanBot.Commands.RandomManga;
import me.MyJikanBot.Commands.Schedule;
import me.MyJikanBot.Commands.Search;
import me.MyJikanBot.Commands.Season;
import me.MyJikanBot.Commands.Top;
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
		jda = new JDABuilder(AccountType.BOT)
				.setToken("INSERT BOT TOKEN" + "").build();
		jda.getPresence().setStatus(OnlineStatus.IDLE);
		jda.getPresence().setActivity(Activity.watching("Mob Psycho 100"));

		jda.addEventListener(new Command());
		jda.addEventListener(new UserInfo());
		jda.addEventListener(new Top());
		jda.addEventListener(new Schedule());
		jda.addEventListener(new Season());
		jda.addEventListener(new RandomAnime());
		jda.addEventListener(new RandomManga());
		jda.addEventListener(new Search());
		jda.addEventListener(new Clear());
		jda.addEventListener(new Kick());
		jda.addEventListener(new GuildMemberJoin());
		jda.addEventListener(new GuildMemberLeave());
		jda.addEventListener(new GuildMessageReceived());
		jda.addEventListener(new GuildMessageReactionAdd());
	}
}
