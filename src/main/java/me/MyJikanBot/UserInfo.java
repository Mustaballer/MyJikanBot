package me.MyJikanBot;

import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class UserInfo extends ListenerAdapter {
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");

		if (args[0].equalsIgnoreCase(Bot.prefix + "user")) {
			Member name = event.getMessage().getMentionedMembers().get(0);
			EmbedBuilder eb = new EmbedBuilder().setColor(0xa6a6ff)
					.setThumbnail("https://win10faq.com/wp-content/uploads/2018/02/microsoft-img.png")
					.setAuthor("Information on " + name.getUser().getName(), "http://www.google.com",
							name.getUser().getAvatarUrl())
					.addField("Status:", name.getOnlineStatus().toString(), true)
					.addField("Roles:", getRolesAsString(name.getRoles()), true)
					.addField("Nickname: ", name.getNickname() == null ? "No Nickname" : name.getNickname(), true);
			event.getChannel().sendTyping().queue();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			event.getChannel().sendMessage(eb.build()).queue();
			eb.clear();
		}
	}

	private String getRolesAsString(List<Role> rolesList) {
		// TODO Auto-generated method stub
		String roles;
		if (!rolesList.isEmpty()) {
			Role tempRole = (Role) rolesList.get(0);
			roles = tempRole.getName();
			for (int i = 1; i < rolesList.size(); i++) {
				tempRole = (Role) rolesList.get(i);
				roles = roles + ", " + tempRole.getName();
			}
		} else {
			roles = "No Roles";
		}
		return roles;
	}
}
