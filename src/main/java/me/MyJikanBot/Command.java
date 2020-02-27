package me.MyJikanBot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Command extends ListenerAdapter {
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		
		if (args[0].equalsIgnoreCase(Bot.prefix + "info")) {
			EmbedBuilder info = new EmbedBuilder();
			info.setTitle("📺  MyJikanBot");
			info.setDescription("This Bot was designed to test out Jikan's API");
			info.addField("!random-anime","command that returns a random anime", true);
			info.addField("!random-manga","command that returns a random manga", true);
			info.addField("!boys","command that returns a secret image", true);
			info.setImage("https://i.imgur.com/ctoJ3Jp.png");
			info.setColor(0xf45642);
			info.setFooter("Created by Mustafa Abdulrahman", event.getMember().getUser().getAvatarUrl());
			
			event.getChannel().sendTyping().queue();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			event.getChannel().sendMessage(info.build()).queue();
			info.clear();
		}
	}
}

