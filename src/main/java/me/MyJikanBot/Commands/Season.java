package me.MyJikanBot.Commands;

import java.io.IOException;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import me.MyJikanBot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Season extends ListenerAdapter {

	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");

		if (args[0].equalsIgnoreCase(Bot.prefix + "season")) {
			if (args.length == 2 || args.length == 1) {
				EmbedBuilder error = new EmbedBuilder();
				error.setColor(0xff3923);
				error.setTitle("🔴 Request has failed");
				error.setDescription(
						"Please specify what season and year using this format: `!season {season} {year}`. Make sure its lowercase.");
				event.getChannel().sendMessage(error.build()).queue();
			}
			// checks to look for season types and year
			String season = null;
			String year = null;
			// subtype arguments for anime
			switch (args[1]) {
			case "summer":
				System.out.println("summer");
				season = "summer";
				break;
			case "spring":
				System.out.println("spring");
				season = "spring";
				break;
			case "fall":
				System.out.println("fall");
				season = "fall";
				break;
			case "winter":
				System.out.println("winter");
				season = "winter";
				break;

			default:
				System.out.println("no match");
				EmbedBuilder error = new EmbedBuilder();
				error.setColor(0xff3923);
				error.setTitle("🔴 Request has failed");
				error.setDescription(
						"Season was not inputted correctly. Make sure to use these: summer, fall, winter, spring ");

				event.getChannel().sendMessage(error.build()).queue();
				season = null;
			}
			// parses int
			int x = Integer.parseInt(args[2]);
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			if (1975 <= x && x <= currentYear ) {
				year = args[2];
				
			} else {
				// when type of either manga or anime isn't specified
				year = null;
				EmbedBuilder error = new EmbedBuilder();
				error.setColor(0xff3923);
				error.setTitle("🔴 Incorrect year requested. Please choose a year between 1970 and 2020");
				error.setDescription("For further clarification please refer to !help");
				event.getChannel().sendMessage(error.build()).queue();
			}

			// sets up client
			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder().url("https://jikan1.p.rapidapi.com/season/"+year+"/"+season)
					.get().addHeader("x-rapidapi-host", "jikan1.p.rapidapi.com")
					.addHeader("x-rapidapi-key", "96a73e158bmshe52e62c5001ba3fp197597jsnaf4ed14bff71").build();

			Response response;
			try {
				response = client.newCall(request).execute();
				String data = response.body().string();
				System.out.println(data);

				// Data of top entries
				JSONObject obj = new JSONObject(data);
				String anime = obj.get("anime").toString();
				System.out.println(anime);

				JSONArray myResponse = new JSONArray(anime);
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
					System.out.println(numMembers[i]);;

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
						error.clear();
					}

				}
				event.getChannel().sendTyping().queue();
				Thread.sleep(1000);
				// embed for searched anime or manga
				EmbedBuilder pic = new EmbedBuilder();
				pic.setColor(0xa6a6ff);
				pic.setTitle("🟦 " + season.toUpperCase() + " " + year.toUpperCase()+ " SEASON");
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
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
