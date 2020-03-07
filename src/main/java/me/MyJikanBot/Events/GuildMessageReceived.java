package me.MyJikanBot.Events;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GuildMessageReceived extends ListenerAdapter {

	public static String take(String d) throws IOException {
		OkHttpClient client = new OkHttpClient();
		// put code here for random id

		Random r = new Random();
		// random number between 10000 and 1
		int random = r.nextInt((10000 - 1) + 1) + 1;
		String id = Integer.toString(random);
		System.out.println(id);

		Request request = new Request.Builder().url("https://jikan1.p.rapidapi.com/anime/" + id + "/pictures").get()
				.addHeader("x-rapidapi-host", "jikan1.p.rapidapi.com")
				.addHeader("x-rapidapi-key", "96a73e158bmshe52e62c5001ba3fp197597jsnaf4ed14bff71").build();
		try {
			Response response = client.newCall(request).execute();
			String dude = response.body().string();
			System.out.println(dude);

			// Initial object to go to pictures
			JSONObject obj = new JSONObject(dude);
			String pictures = obj.get("pictures").toString();
			System.out.println(pictures);

			// this is index of the array of pics
			JSONArray myResponse = new JSONArray(pictures);
			String index = myResponse.get(0).toString();
			System.out.println(index);

			// Final url of large image
			JSONObject size = new JSONObject(index);
			String link = size.get("large").toString();
			System.out.println(link);
			return link;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");

		if (args[0].equalsIgnoreCase("!boys")) {

			// Get channel name and send in current channel
			String channelName = event.getChannel().getName();
			event.getChannel().sendMessage(channelName).queue();
			event.getChannel().sendTyping().queue();

			// Upload and send an image to current channel
			try {
				URL url = new URL(
						"https://cdn.vox-cdn.com/thumbor/ErtVO6Ub8PiWxplqkehZyrXqdAQ=/0x0:2000x800/1200x800/filters:focal(854x58:1174x378)/cdn.vox-cdn.com/uploads/chorus_image/image/61878909/5433_Tier03_SeriesHeader_20C_2000x800.0.jpg");
				BufferedImage img = ImageIO.read(url);
				File file = new File("testserver_image_20thcenturyboys.jpg"); // change the '.jpg' to whatever
																				// extensions the image has
				ImageIO.write(img, "jpg", file); // again, change 'jpg' to the correct extension
				event.getChannel().sendFile(file).queue();

				EmbedBuilder hidden = new EmbedBuilder();
				hidden.setColor(0xff1923);
				hidden.setTitle("💥Welcome to the real club: 20th Century Boys");
				hidden.setDescription("You have now been appointed as a Manga reader. Rejoice! You can now use the Rythm bot in the audio-command channel.");
				event.getChannel().sendTyping().queue();
				Thread.sleep(1000);
				event.getChannel().sendMessage(hidden.build()).queue();
				hidden.clear();
				// Add role
				event.getGuild().modifyMemberRoles(event.getMember(), event.getGuild().getRolesByName("Manga-readers", true))
						.complete();
			} catch (Exception e) {
				event.getChannel().sendMessage("Error fetching image.").queue();
			}
		}

		if (args[0].equalsIgnoreCase("!rpic")) {
			Boolean missing = false;

			String test = "Replace some characters!";
			try {
				test = take(test);
				if (test == null) {
					missing = true;
				}

			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if (missing == false && test != null) {
				// Upload and send an image to current channel
				try {
					event.getChannel().sendTyping().queue();
					try {
						// take a 4 second delay
						Thread.sleep(4000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					EmbedBuilder pic = new EmbedBuilder();
					pic.setColor(0xff3923);
					pic.setThumbnail("https://pbs.twimg.com/profile_images/1190380284295950339/Py6XnxvH_400x400.jpg");
					pic.setTitle("🐱‍👤 Check out this random pic");
					pic.setImage(test);
					pic.setDescription("Random image from MAL database");
					event.getChannel().sendMessage(pic.build()).queue();
					pic.clear();
				} catch (Exception e) {
					event.getChannel().sendMessage("Error fetching image.").queue();
				}
			} else {
				EmbedBuilder error = new EmbedBuilder();
				error.setColor(0xff3923);
				error.setTitle("🔴 There appears to be no picture for this random anime");
				error.setDescription("Anime id contains no image in the MAL database");
				event.getChannel().sendMessage(error.build()).queue();
				error.clear();
			}

		}
		/*
		 * Part 5- Mute Command--> find replacement for add role and removing roles, use
		 * online documentation try this link
		 *
		 * https://ci.dv8tion.net/job/JDA/javadoc/net/dv8tion/jda/api/entities/Member.
		 * html#getRoles()
		 *
		 *
		 */
		if (args[0].equalsIgnoreCase("!mute")) {
			if (args.length == 2) {
				Member member = event.getGuild().getMemberById(args[1].replace("<@", "").replace(">", ""));
				Role role = event.getGuild().getRoleById("679519729307877396");

				// debugging purposes
				event.getChannel().sendMessage("Muted" + args[1] + ".").queue();

				if (member.getRoles().contains(role) == false) {
					// Mute user
					event.getChannel().sendMessage("Muted" + args[1] + ".").queue();

					// Add role
					event.getGuild().modifyMemberRoles(event.getMember(), event.getGuild().getRolesByName("Muted", true))
							.complete();
				} else {
					// Unmute user
					// remove role
					event.getGuild().modifyMemberRoles(event.getMember(), event.getGuild().getPublicRole())
							.complete();					

				}
			} else if (args.length == 3) {
				Member member = event.getGuild().getMemberById(args[1].replace("<@", "").replace(">", ""));
				Role role = event.getGuild().getRoleById("679519729307877396");

				event.getChannel().sendMessage("Muted" + args[1] + "for" + args[2] + " seconds.").queue();
				// Add role
				event.getGuild().modifyMemberRoles(event.getMember(), event.getGuild().getRolesByName("Muted", true))
						.complete();

				// Unmute after a few seconds
				new java.util.Timer().schedule(new java.util.TimerTask() {
					@Override
					public void run() {
						// TODO Auto-generated method stub

						event.getGuild().modifyMemberRoles(event.getMember(), event.getGuild().getPublicRole())
						.complete();
						// event.getGuild().getController().removeRolesFromMember(member,
						// role).complete();
					}
				}, Integer.parseInt(args[2]) * 1000

				);
			} else {
				event.getChannel().sendMessage("Incorrect syntax. Use `!mute [user mention] [time {optional}]`")
						.queue();
			}

		}

	}
}
