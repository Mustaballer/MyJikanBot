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

public class Top extends ListenerAdapter {

	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");

		if (args[0].equalsIgnoreCase(Bot.prefix + "top")) {
			if (args.length == 1 || args.length == 2 && !args[1].equalsIgnoreCase("manga")) {
				EmbedBuilder error = new EmbedBuilder();
				error.setColor(0xff3923);
				error.setTitle("🔴 Request has failed");
				error.addField("Use these subtypes",
						"For Anime: airing, upcoming, tv, movie, ova, special. For Manga: manga, novels, oneshots, doujin, manhwa, manhua. Both: bypopularity, favorite.",
						false);
				event.getChannel().sendMessage(error.build()).queue();
			}
			// checks to look for manga or anime entries
			String type = null;
			String subtype = null;
			if (args[1].equalsIgnoreCase("anime")) {
				type = "anime";
				// subtype arguments for anime
				switch (args[2]) {
				case "airing":
					System.out.println("airing");
					subtype = "airing";
					break;
				case "upcoming":
					System.out.println("upcoming");
					subtype = "upcoming";
					break;
				case "tv":
					System.out.println("tv");
					subtype = "tv";
					break;
				case "movie":
					System.out.println("movie");
					subtype = "movie";
					break;
				case "ova":
					System.out.println("ova");
					subtype = "ova";
					break;
				case "special":
					System.out.println("special");
					subtype = "special";
					break;
				case "bypopularity":
					System.out.println("bypopularity");
					subtype = "bypopularity";
					break;
				case "favorite":
					System.out.println("favorite");
					subtype = "favorite";
					break;
				default:
					System.out.println("no match");
					EmbedBuilder error = new EmbedBuilder();
					error.setColor(0xff3923);
					error.setTitle("🔴 Request has failed");
					error.addField("Use these subtypes",
							"For Anime: airing, upcoming, tv, movie, ova, special. For Manga: manga, novels, oneshots, doujin, manhwa, manhua. Both: bypopularity, favorite.",
							false);
					event.getChannel().sendMessage(error.build()).queue();
					subtype = null;
				}
			} else if (args[1].equalsIgnoreCase("manga")) {
				type = "manga";
				// no need for an extra manga
				if (args.length == 2 || args[2].equalsIgnoreCase("manga")) {
					System.out.println("manga");
					subtype = "manga";
				} else {
					// subtype arguments for manga
					switch (args[2]) {
					case "novels":
						System.out.println("novels");
						subtype = "novels";
						break;
					case "oneshots":
						System.out.println("oneshots");
						subtype = "oneshots";
						break;
					case "doujin":
						System.out.println("doujin");
						subtype = "doujin";
						break;
					case "manhwa":
						System.out.println("manhwa");
						subtype = "manhwa";
						break;
					case "manhua":
						System.out.println("manhua");
						subtype = "manhua";
						break;
					case "bypopularity":
						System.out.println("bypopularity");
						subtype = "bypopularity";
						break;
					case "favorite":
						System.out.println("favorite");
						subtype = "favorite";
						break;
					default:
						System.out.println("no match");
						subtype = null;
						EmbedBuilder error = new EmbedBuilder();
						error.setColor(0xff3923);
						error.setTitle("🔴 Request has failed");
						error.addField("Use these subtypes",
								"For Anime: airing, upcoming, tv, movie, ova, special. For Manga: manga, novels, oneshots, doujin, manhwa, manhua. Both: bypopularity, favorite.",
								false);
						event.getChannel().sendMessage(error.build()).queue();
					}
				}
			} else {
				// when type of either manga or anime isn't specified
				type = null;
				subtype = null;
				EmbedBuilder error = new EmbedBuilder();
				error.setColor(0xff3923);
				error.setTitle("🔴 Incorrect type. Please refer to !help for proper request call");
				error.setDescription("`types include: anime or manga`");
				event.getChannel().sendMessage(error.build()).queue();
			}

			/*
			 * Anime: airing upcoming tv movie ova special Manga: manga novels oneshots
			 * doujin manhwa manhua Both: bypopularity favorite
			 */

			// sets up client
			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder().url("https://jikan1.p.rapidapi.com/top/" + type + "/1/" + subtype)
					.get().addHeader("x-rapidapi-host", "jikan1.p.rapidapi.com")
					.addHeader("x-rapidapi-key", "96a73e158bmshe52e62c5001ba3fp197597jsnaf4ed14bff71").build();

			Response response;
			try {
				response = client.newCall(request).execute();
				String data = response.body().string();
				System.out.println(data);

				// Data of top entries
				JSONObject obj = new JSONObject(data);
				String top = obj.get("top").toString();
				System.out.println(top);

				JSONArray myResponse = new JSONArray(top);
				// 5 responses
				String[] indexes = new String[5];
				String[] titles = new String[5];
				String[] scores = new String[5];
				String[] numMembers = new String[5];
				String[] ranks = new String[5];
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

					// Finds rank
					JSONObject ranker = new JSONObject(indexes[i]);
					ranks[i] = ranker.get("rank").toString();
					System.out.println(ranks[i]);

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
						if (ranks[i] == null) {
							ranks[i] = "unavailable";
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
				// replace manga subtype to avoid repetition as 'series'
				if (subtype == "manga") {
					subtype = "series";	
				}
				event.getChannel().sendTyping().queue();
				Thread.sleep(1000);
				// embed for searched anime or manga
				EmbedBuilder pic = new EmbedBuilder();
				pic.setColor(0xa6a6ff);
				pic.setTitle("🟦 TOP 5 " + type.toUpperCase() + " " + subtype.toUpperCase());
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
