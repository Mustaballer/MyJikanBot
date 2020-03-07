package me.MyJikanBot.Commands;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import me.MyJikanBot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Schedule extends ListenerAdapter {

	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");

		if (args[0].equalsIgnoreCase(Bot.prefix + "schedule")) {
			if (args.length == 1) {
				EmbedBuilder error = new EmbedBuilder();
				error.setColor(0xff3923);
				error.setTitle("🔴Request day was not mentioned, use after !schedule");
				error.addField("Use these days", "monday, tuesday, wednesday, thursday, friday, saturday, sunday",
						false);
				event.getChannel().sendMessage(error.build()).queue();
			}
			// checks to look for manga or anime entries
			String day = null;
			switch (args[1]) {
			case "monday":
				System.out.println("monday");
				day = "monday";
				break;
			case "tuesday":
				System.out.println("tuesday");
				day = "tuesday";
				break;
			case "wednesday":
				System.out.println("wednesday");
				day = "wednesday";
				break;
			case "thursday":
				System.out.println("thursday");
				day = "thursday";
				break;
			case "friday":
				System.out.println("friday");
				day = "friday";
				break;
			case "saturday":
				System.out.println("saturday");
				day = "saturday";
				break;
			case "sunday":
				System.out.println("sunday");
				day = "sunday";
				break;
			default:
				System.out.println("incorrect day");
				EmbedBuilder error = new EmbedBuilder();
				error.setColor(0xff3923);
				error.setTitle("🔴Request day is incorrect format, use these after !schedule");
				error.addField("Only these days", "monday, tuesday, wednesday, thursday, friday, saturday, sunday",
						false);
				event.getChannel().sendMessage(error.build()).queue();
				day = null;
			}

			// sets up client
			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder().url("https://jikan1.p.rapidapi.com/schedule/" + day.toLowerCase()).get()
					.addHeader("x-rapidapi-host", "jikan1.p.rapidapi.com")
					.addHeader("x-rapidapi-key", "96a73e158bmshe52e62c5001ba3fp197597jsnaf4ed14bff71").build();

			Response response;
			try {
				response = client.newCall(request).execute();
				String data = response.body().string();
				System.out.println(data);

				// Data of top entries
				JSONObject obj = new JSONObject(data);
				String selectDay = obj.get(day.toLowerCase()).toString();
				System.out.println(selectDay);

				JSONArray myResponse = new JSONArray(selectDay);
				// 5 responses
				String[] indexes = new String[5];
				String[] titles = new String[5];
				String[] scores = new String[5];
				String[] numMembers = new String[5];
				String[] urls = new String[5];

				for (int i = 0; i < 5; i++) {
					// gets i index
					indexes[i] = myResponse.get(i).toString();
					System.out.println(indexes[i]);

					// Finds title of anime
					JSONObject text = new JSONObject(indexes[i]);
					titles[i] = text.get("title").toString();
					System.out.println(titles[i]);

					// Finds MyAnimeList score
					JSONObject parseImage = new JSONObject(indexes[i]);
					scores[i] = parseImage.get("score").toString();
					System.out.println(scores[i]);

					// Finds number of members
					JSONObject memberCount = new JSONObject(indexes[i]);
					numMembers[i] = memberCount.get("members").toString();
					System.out.println(numMembers[i]);


					// Finds MyAnimeList url
					JSONObject summary = new JSONObject(indexes[i]);
					urls[i] = summary.get("url").toString();
					System.out.println(urls[i]);

					Boolean missing = false;
					if (titles[i] == null || urls[i] == null || numMembers[i] == null) {
						missing = true;
					}

					// checks if command can be done
					if (missing == false && titles[i] != null && urls[i] != null && numMembers[i] != null) {
						// get channel name and send in current channel
						if (scores[i] == null) {
							scores[i] = "unavailable";
						}

						try {
							try {
								// take a 4 second delay
								Thread.sleep(200);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						EmbedBuilder error = new EmbedBuilder();
						error.setColor(0xff3923);
						error.setTitle("🔴 There is an error in retrieving the requested information");
						error.setDescription("Please refer back to !help for proper request call");
						event.getChannel().sendMessage(error.build()).queue();
					}

				}

				// embed to deliver results of the schedule
				event.getChannel().sendTyping().queue();
				EmbedBuilder pic = new EmbedBuilder();
				pic.setColor(0xa6a6ff);
				pic.setTitle("🟦 SCHEDULE FOR " + day.toUpperCase());
				pic.setThumbnail("https://pbs.twimg.com/profile_images/1190380284295950339/Py6XnxvH_400x400.jpg");
				pic.addField(titles[0],
						 " score: " + scores[0] + " members: " + numMembers[0] + " url: " + urls[0], false);
				pic.addField(titles[1],
						 " score: " + scores[1] + " members: " + numMembers[1] + " url: " + urls[1], false);
				pic.addField(titles[2],
						 " score: " + scores[2] + " members: " + numMembers[2] + " url: " + urls[2], false);
				pic.addField(titles[3],
						 " score: " + scores[3] + " members: " + numMembers[3] + " url: " + urls[3], false);
				pic.addField(titles[4],
						 " score: " + scores[4] + " members: " + numMembers[4] + " url: " + urls[4], false);
				event.getChannel().sendMessage(pic.build()).queue();
				pic.clear();

			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

		}

	}
}
