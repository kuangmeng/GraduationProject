package mengseg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import uno.meng.Clause;
import uno.meng.MengPOSTagging;
import uno.meng.MengSeg;
 
@Path("/postagging")
public class POSTagging {
    public static MengPOSTagging postagging = null;
    public POSTagging() throws IOException {
      postagging = new MengPOSTagging();
    }
	@POST
	@Produces("application/json")
	public Response getPOSTagging(String str) throws IOException{
      str = str.trim();
	  List<String> claused = new ArrayList<String>();
      String result = "";
      if (str.length() > 30) {
        claused = Clause.getClause(str);
      } else {
        claused.add(str);
      }
      for (String s : claused) {
        List<String> segged = Segment.seg.Seg(s);
        List<String> tagged = postagging.POSTagging(segged);
         for(int i =0; i< segged.size()-1;i++) {
           result = result + segged.get(i)+"_"+tagged.get(i)+" ";
         }
         result = result + segged.get(segged.size()-1)+"_"+tagged.get(tagged.size()-1)+"\n";
      }
      
      return Response.status(200).entity(result).build();
      }
}
