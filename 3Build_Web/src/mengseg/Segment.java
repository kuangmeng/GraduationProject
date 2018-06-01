package mengseg;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import uno.meng.Clause;
import uno.meng.MengSeg;
import uno.meng.crf_seg.library.CrfLibrary;

 
@Path("/seg")
public class Segment {
      public static MengSeg seg = CrfLibrary.get();
	  @POST
	  @Produces("application/json")
	  public Response getSegment(String str){
	    str = str.trim();
        List<String> claused = new ArrayList<String>();
        String result = "";
	    if (str.length() > 30) {
          claused = Clause.getClause(str);
        } else {
          claused.add(str);
        }
        for (String s : claused) {
          List<String> segged = seg.Seg(s);

          for(int i = 0;i < segged.size()-1;i++) {
            result = result + segged.get(i)+" ";
          }
          result = result + segged.get(segged.size()-1)+"\n";
        }
        
		return Response.status(200).entity(result).build();
	  }
	  
}
