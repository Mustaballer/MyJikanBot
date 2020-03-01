package me.MyJikanBot.Commands;

import org.json.JSONArray;
import org.json.JSONObject;

import me.MyJikanBot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Search extends ListenerAdapter {

	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");

		if (args[0].equalsIgnoreCase(Bot.prefix + "search")) {
			if (args.length == 2) {
				EmbedBuilder error = new EmbedBuilder();
				error.setColor(0xff3923);
				error.setTitle("🔴 Incorrect Search query");
				error.setDescription("'!search {anime/manga} {title}'");
				event.getChannel().sendMessage(error.build()).queue();
			}
			// checks to look for manga or anime entries
			String type = null;
			if (args[1].equalsIgnoreCase("anime")) {
				type = "anime";
			}
			if (args[1].equalsIgnoreCase("manga")) {
				type = "manga";
			}

			String searchInquiry = "";

			// tests to incorporate spaces using same restrictions
			for (int i = 2; i < args.length; i++) {
				// if element is less than last element
				if (i < args.length - 1) {
					// problem is here
					if (i == 2) {
						searchInquiry = searchInquiry + args[i] + "-" + args[i + 1];
					} else {
						searchInquiry = searchInquiry + "-" + args[i + 1];
					}
				} else if (args.length == 3) {
					searchInquiry = args[2];
				}
				System.out.println(searchInquiry);
			}

			searchInquiry = searchInquiry.replaceAll("-", "%20");
			System.out.println(searchInquiry);

			// sets up client
			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder()
					.url("https://jikan1.p.rapidapi.com/search/" + type + "?page=1&q=" + searchInquiry).get()
					.addHeader("x-rapidapi-host", "jikan1.p.rapidapi.com")
					.addHeader("x-rapidapi-key", "RAPID API KEY").build();

			try {
				Response response = client.newCall(request).execute();
				String data = response.body().string();
				System.out.println(data);

				// Data of genre anime entries
				JSONObject obj = new JSONObject(data);
				String results = obj.get("results").toString();
				System.out.println(results);

				JSONArray myResponse = new JSONArray(results);
				// gets random index and parses details
				String index = myResponse.get(0).toString();
				System.out.println(index);

				// Finds title of anime
				JSONObject text = new JSONObject(index);
				String title = text.get("title").toString();
				System.out.println(title);

				// Finds image url
				JSONObject parseImage = new JSONObject(index);
				String imageUrl = parseImage.get("image_url").toString();
				System.out.println(imageUrl);

				// Finds synopsis
				JSONObject summary = new JSONObject(index);
				String synopsis = summary.get("synopsis").toString();
				System.out.println(synopsis);

				Boolean missing = false;
				if (title == null || imageUrl == null || synopsis == null) {
					missing = true;
				}

				// checks if command can be done
				if (missing == false && title != null && imageUrl != null && synopsis != null) {
					// get channel name and send in current channel
					String channelName = event.getChannel().getName();
					event.getChannel().sendMessage(channelName).queue();
					event.getChannel().sendTyping().queue();

					try {
						try {
							// take a 4 second delay
							Thread.sleep(4000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						// embed for searched anime or manga
						EmbedBuilder pic = new EmbedBuilder();
						pic.setColor(0xa6a6ff);
						pic.setThumbnail("https://pbs.twimg.com/profile_images/1190380284295950339/Py6XnxvH_400x400.jpg");
						pic.setTitle("🟦 "+title);
						pic.setImage(imageUrl);
						pic.addField("Synopsis", synopsis, true);
						event.getChannel().sendMessage(pic.build()).queue();
						pic.clear();
					} catch (Exception e) {
						event.getChannel().sendMessage("Error fetching data.").queue();
					}
				} else {
					EmbedBuilder error = new EmbedBuilder();
					error.setColor(0xff3923);
					error.setTitle("🔴 There is an error in retrieving the requested information");
					error.setDescription("Make sure to replace spaces with `-`. This is case sensitive for now.");
					event.getChannel().sendMessage(error.build()).queue();
				}
			} catch (Exception e) {
				System.out.println(e);
			}

		}

	}
}
