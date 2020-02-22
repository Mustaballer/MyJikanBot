package me.MyJikanBot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Command extends ListenerAdapter {
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		
		if (args[0].equalsIgnoreCase(Bot.prefix + "info")) {
			EmbedBuilder info = new EmbedBuilder();
			info.setTitle("📺 MyJikanBot");
			info.setDescription("This Bot was designed to test out Jikan's API");
			info.setColor(0xf45642);
			info.setFooter("Created by Mustafa Abdulrahman", event.getMember().getUser().getAvatarUrl());
			
			event.getChannel().sendTyping().queue();
			event.getChannel().sendMessage(info.build()).queue();
			info.clear();
		}
	}
}

