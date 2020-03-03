package me.MyJikanBot.Commands;

import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import me.MyJikanBot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RandomAnime extends ListenerAdapter {

	public static String[] myData() {
		// Do something here

		OkHttpClient client = new OkHttpClient();
		// put code here for random id

		Random r = new Random();
		// random number for id genre
		// exclude id 12, 26, 9(may keep), 28, 33, 34
		int random = r.nextInt((43 - 1) + 1) + 1;

		while (random == 12 || random == 26 || random == 9 || random == 28 || random == 33 || random == 34) {
			random = r.nextInt((43 - 1) + 1) + 1;
		}

		String id = Integer.toString(random);
		System.out.println(id);

		Request request = new Request.Builder().url("https://jikan1.p.rapidapi.com/genre/anime/" + id + "/1").get()
				.addHeader("x-rapidapi-host", "jikan1.p.rapidapi.com")
				.addHeader("x-rapidapi-key", "96a73e158bmshe52e62c5001ba3fp197597jsnaf4ed14bff71").build();
		try {
			Response response = client.newCall(request).execute();
			String data = response.body().string();
			System.out.println(data);

			// Data of genre anime entries
			JSONObject obj = new JSONObject(data);
			String anime = obj.get("anime").toString();
			System.out.println(anime);

			JSONArray myResponse = new JSONArray(anime);
			// this is for random index out of 99 anime in the genre
			Random rnd = new Random();
			int rand = rnd.nextInt((99 - 0) + 1) + 0;

			// gets random index and parses details
			String index = myResponse.get(rand).toString();
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

			return new String[] { title, imageUrl, synopsis }; // returns array of necessary values

		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");

		if (args[0].equalsIgnoreCase(Bot.prefix + "random-anime")) {

			Boolean missing = false;

			String title = "Replace some characters!";
			String imageUrl = "Replace some characters!";
			String synopsis = "Replace some characters!";
			try {
				String[] parsed = myData(); // invoking method and storing returned array into parsed string array
				title = parsed[0];
				imageUrl = parsed[1];
				synopsis = parsed[2];

				if (title == null || imageUrl == null || synopsis == null) {
					missing = true;
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// checks if command can be done
			if (missing == false && title != null && imageUrl != null && synopsis != null) {

				try {
					event.getChannel().sendTyping().queue();
					try {
						// take a 4 second delay
						Thread.sleep(4000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// embed for random anime
					EmbedBuilder pic = new EmbedBuilder();
					pic.setColor(0xa6a6ff);
					pic.setThumbnail("https://pbs.twimg.com/profile_images/1190380284295950339/Py6XnxvH_400x400.jpg");
					pic.setTitle("🟦 "+title);
					pic.setImage(imageUrl);
					pic.addField("Synopsis", synopsis, true);
					event.getChannel().sendMessage(pic.build()).queue();
					pic.clear();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				EmbedBuilder error = new EmbedBuilder();
				error.setColor(0xff3923);
				error.setTitle("🔴 There is an error in retrieving the requested information");
				error.setDescription("MAL database could not find the data");
				event.getChannel().sendTyping().queue();
				event.getChannel().sendMessage(error.build()).queue();
				error.clear();
			}

		}

	}

}
