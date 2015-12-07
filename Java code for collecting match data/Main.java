package dota;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

	// use key-value pairs of <int, int[]> to keep track of matches & players
	public static int currentId = 0;
	public static HashMap<Integer, int[]> matchData = new HashMap<Integer, int[]>();

	public static void main(String[] args){
		
		loadData();
		// decide how many times to query the valve API
		for(int i = 0; i < 1; i ++){
			updateMatches(2);
			updateMatches(3);

		}
		
		
		try(PrintWriter printOut = new PrintWriter(new BufferedWriter(new FileWriter("./src/dota/match_ids.txt", true)))) {
			
			for(int keys : matchData.keySet()){
				System.out.print(keys + " ");
				printOut.print(keys+ " ");
				for(int j = 0; j < 10; j++){
					System.out.print(matchData.get(keys)[j] + " ");
					printOut.print(matchData.get(keys)[j] + " ");
				}
				System.out.println();
				printOut.println();
			}

		}catch (IOException e) {
		}


	}

	public static void updateMatches(int skillNum){

		String originalURL = "https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/V001/?key=2F707DE3A6331E50733B7FEF82411020&skill=" + skillNum;
		URL url;
		String matchString = "";
		HttpURLConnection urlConnection = null;
		try {
			if(currentId == 0){
				url = new URL(originalURL);
			}else{
				url = new URL(originalURL + "&start_at_match_id="+currentId);
			}
			urlConnection = (HttpURLConnection) url.openConnection();
			matchString = readStream(urlConnection.getInputStream());
			JSONObject jsonString = new JSONObject(matchString);
			JSONObject jsonResult = jsonString.getJSONObject("result");
			JSONArray jsonArray = jsonResult.getJSONArray("matches");
			JSONObject matchObject = new JSONObject();
			JSONArray playersArray = new JSONArray();

			for(int i = 0; i < jsonArray.length(); i++){
				matchObject = jsonArray.getJSONObject(i);
				playersArray = matchObject.getJSONArray("players");
				boolean abandons = false;
				int[] players = new int[10];
				for(int j = 0; j < playersArray.length(); j++){
					players[j] = playersArray.getJSONObject(j).getInt("hero_id");
					
					// make sure the match didn't have any abandons
					if(players[j] == 0){
						abandons = true;
					}
				}

				// make sure it's 5v5 public matchmaking (instead of something like 1v1)
				if(matchObject.getInt("lobby_type") == 0 && abandons == false){
					matchData.put(matchObject.getInt("match_id"), players);
				}
			}
			
			currentId = matchObject.getInt("match_id");

		} catch (Exception e) {
			System.out.println(e);;
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
	}
	
	public static String readStream(InputStream in) {
		BufferedReader reader = null;
		StringBuffer response = new StringBuffer();
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}
		return response.toString();
	}
	
	public static void loadData(){
		
		
		String File = "./src/dota/match_ids.txt";
		BufferedReader br = null;
		String line = "";
		String splitBy = " ";

		try {

			br = new BufferedReader(new FileReader(File));
			while ((line = br.readLine()) != null) {

				String[] stringList;
				stringList = line.split(splitBy);

				for(int i = 0; i < stringList.length; i++){
					int matchId = 0;
					int[] players = new int[10];
					if(i == 0){
						matchId = Integer.parseInt(stringList[i]);
					}
					else{
						players[i-1] = Integer.parseInt(stringList[i]);
					}
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return;
	}
}
