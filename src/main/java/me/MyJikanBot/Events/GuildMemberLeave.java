package me.MyJikanBot.Events;

import java.util.Random;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMemberLeave extends ListenerAdapter {
	String[] messages = { "[member] has left, the party is over..." };

	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {

		// new code
		Random rand = new Random();
		int number = rand.nextInt(messages.length);

		EmbedBuilder join = new EmbedBuilder();
		join.setColor(0xf48342);
		join.setDescription(messages[number].replace("[member]", event.getMember().getAsMention()));

		event.getGuild().getTextChannelById("683149636537810959").sendMessage(join.build()).queue();
	}
}
