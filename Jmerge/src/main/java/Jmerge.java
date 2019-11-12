import java.io.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONWriter;

public class Jmerge
{
    private static JSONObject merger=new JSONObject();
    private static ArrayList listforkeys=new ArrayList();
    private static ArrayList<HashMap<JSONObject,String>> listforvalues=new ArrayList();

    private static void jsonMergerUtility() throws Exception {

        File dir = new File("C:\\Users\\robin\\IdeaProjects\\_123\\files");

        for (File file : Objects.requireNonNull(dir.listFiles())) {

            JSONObject obj = new JSONObject(new JSONTokener(new FileReader(file)));
            splitKeyValuePairs(obj);
        }
        Set set=new HashSet(listforkeys);
        JSONObject jb=jsonMerger(set, listforvalues);
        String prettyJson = crunchifyPrettyJSONUtility(jb.toString());
        System.out.println("\nPretty JSON Result:\n" + prettyJson);

         writetoFile(jb);
    }

    /* it splits all key-value pairs and stores them to keys list and values list*/

    private static void splitKeyValuePairs(JSONObject source){
        for (String key: JSONObject.getNames(source)) {
            Object value = source.get(key);
            listforkeys.add(key);
            JSONArray jsonArray=new JSONArray(value.toString());
            for (int i = 0; i <jsonArray.length() ; i++) {
                HashMap map=new LinkedHashMap();
                map.put(jsonArray.getJSONObject(i),key);
                listforvalues.add(map);
            }
        }
    }

    /* jsonMerger() maps array of json objects according to its key value*/

    private static JSONObject jsonMerger(Set set,ArrayList list){
        Iterator iterator=set.iterator();
        while (iterator.hasNext()){
            JSONArray jsonArray=new JSONArray();
            String s=(String) iterator.next();
            for (int i = 0; i <list.size() ; i++)
            {
                if(s.equals(getValue(list.get(i)))){
                    jsonArray.put(getKey(list.get(i)));
                }

            }
            merger.put(s,jsonArray);
        }
        return merger;
    }

    private static String getKey(Object o){
        HashMap map=new HashMap((HashMap)o);
        String s="";
        for(Object ob:map.keySet()){
            s+=ob;
        }

        return s;
    }
    private static String getValue(Object o){
        HashMap map=new HashMap((HashMap)o);
        String s="";
        for(Object ob:map.values()){
            s+=ob;
        }
        return s;
    }
/* Writes merged objects to Merge.json*/
    private static void writetoFile(JSONObject jb){
        String filename="files/Merge.json";

        try  {
            FileWriter file = new FileWriter(filename);
            for(String keys:JSONObject.getNames(jb)){
                Object val=jb.get(keys);
                new JSONWriter(file)
                        .object()
                        .key(keys)
                        .value(val.toString())
                        .endObject();
            }
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /* Makes merged json file as  human readable*/

    private static String crunchifyPrettyJSONUtility(String simpleJSON) {
        JsonParser crunhifyParser = new JsonParser();
        JsonObject json = crunhifyParser.parse(simpleJSON).getAsJsonObject();

        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();

        return prettyGson.toJson(json);
    }
    public static void main(String[] args) throws Exception {
        jsonMergerUtility();
    }
}


