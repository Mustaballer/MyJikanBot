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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;


public class Bot {
	public static JDA jda;
	public static String prefix = "!";
	
	// Main Method
	public static void main(String[] args) throws LoginException {
		jda = new JDABuilder(AccountType.BOT).setToken("Njc2NjAxMzQwOTk4NDUxMjIw.XlGffw.KJG7EBss4K_AN2W5V-4X9egV76s").build();
		jda.getPresence().setStatus(OnlineStatus.IDLE);
		jda.getPresence().setActivity(Activity.watching("20th century boys"));

		
		jda.addEventListener(new Clear());
		jda.addEventListener(new GuildMemberJoin());
		jda.addEventListener(new GuildMemberLeave());
		jda.addEventListener(new GuildMessageReceived());
		jda.addEventListener(new GuildMessageReactionAdd());

		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
				.url("https://jikan1.p.rapidapi.com/meta/requests/anime/today")
				.get()
				.addHeader("x-rapidapi-host", "jikan1.p.rapidapi.com")
				.addHeader("x-rapidapi-key", "96a73e158bmshe52e62c5001ba3fp197597jsnaf4ed14bff71")
				.build();

		try {
			Response response = client.newCall(request).execute();
			System.out.println(response.body().string());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
