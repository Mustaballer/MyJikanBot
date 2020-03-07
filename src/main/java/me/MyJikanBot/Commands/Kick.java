package me.MyJikanBot.Commands;
import java.util.List;

import me.MyJikanBot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Kick extends ListenerAdapter {
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");

		Role admin = event.getGuild().getRoleById("683137305409749004");

		if (args[0].equalsIgnoreCase(Bot.prefix + "kick") && event.getMember().getRoles().contains(admin)) {
			if (args.length < 2) {
				// Usage
				EmbedBuilder usage = new EmbedBuilder();
				usage.setColor(0xff3923);
				usage.setTitle("Specify who to kick");
				usage.setDescription("Usage: `" + Bot.prefix + "kick @[username]`");
				event.getChannel().sendMessage(usage.build()).queue();
			} else {
				try {
					TextChannel channel = event.getChannel();
					Member member = event.getMember();
					List<Member> mentionedMembers = event.getMessage().getMentionedMembers();

					if (args.length == 0 || mentionedMembers.isEmpty()) {
						channel.sendMessage("Missing arguments").queue();
						return;
					}

					Member target = mentionedMembers.get(0);
					
					if (!event.getMember().getRoles().contains(admin) || !member.canInteract(target)) {
						channel.sendMessage("You don't have permission to use this command").queue();
						return;
					}

					event.getGuild()
							.kick(target, String.format("Kick by: %#s", event.getAuthor()))
							.queue();

					channel.sendMessage("Success!").queue();

				} catch (IllegalArgumentException e) {
					System.out.println(e);
					EmbedBuilder error = new EmbedBuilder();
					error.setColor(0xff3923);
					error.setTitle("🔴 Something has gone wrong");
					error.setDescription("Member cannot be kicked out");
					event.getChannel().sendMessage(error.build()).queue();
					error.clear();
				}
			}
		} else if (args[0].equalsIgnoreCase(Bot.prefix + "kick")) {
			EmbedBuilder error = new EmbedBuilder();
			error.setColor(0xff3923);
			error.setTitle("🔴 You do not hold permissions to use this command");
			error.setDescription("Only the Admin can kick members");
			event.getChannel().sendMessage(error.build()).queue();
			error.clear();
		}

	}
}
