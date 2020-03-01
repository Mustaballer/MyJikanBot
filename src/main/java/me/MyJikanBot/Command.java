package me.MyJikanBot;

import java.awt.Color;

import javax.xml.soap.MessageFactory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Command extends ListenerAdapter {
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");

		if (args[0].equalsIgnoreCase(Bot.prefix + "info") || args[0].equalsIgnoreCase(Bot.prefix + "help")) {
			// wait
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			event.getAuthor().openPrivateChannel().queue(channel -> {
				EmbedBuilder info = new EmbedBuilder();
				info.setTitle("➿  MyJikanBot");
				info.setDescription("This Bot was designed to test out Jikan's API");
				info.setThumbnail("https://webstockreview.net/images/information-clipart-1.png");
				info.addField("!search {anime/manga} {title}", "query that retrieves information about an anime/manga",
						false);
				info.addField("!top {anime/manga} {subtype}", "command that retrieves ranked data",
						false);
				info.addField("`subtypes`", "For Anime: airing, upcoming, tv, movie, ova, special. For Manga: manga, novels, oneshots, doujin, manhwa, manhua.                  Both: bypopularity, favorite.",
						false);
				info.addField("!random-anime", "command that returns a random anime", true);
				info.addField("!random-manga", "command that returns a random manga", true);
				info.addField("!rpic", "command that returns a random image", true);
				info.addField("!user @{username}", "command that returns generic info about member", false);
				info.setImage("https://i.imgur.com/ctoJ3Jp.png");
				info.setColor(0xa6a6ff);
				info.addField("Creator", "Mustafa Abdulrahman \n https://github.com/Mustaballer/MyJikanBot", false);
				info.setFooter("Used JDA, Gradle, Jikan REST API, Discord", event.getMember().getUser().getAvatarUrl());

				event.getChannel().sendTyping().queue();
			    channel.sendMessage(info.build()).queue();
			    info.clear();
			});
			
		}
	}
}
