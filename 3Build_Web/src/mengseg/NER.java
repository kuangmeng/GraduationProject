package mengseg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import uno.meng.Clause;
import uno.meng.MengNER;
import uno.meng.MengPOSTagging;
import uno.meng.MengSeg;
import uno.meng.ner.domain.Term;

@Path("/ner")
public class NER {
     
     @POST
     @Produces("application/json")
     public Response getNER(String str) throws IOException{
       str = str.trim();
       String res = "";
       List<String> claused = new ArrayList<String>();
       if (str.length() > 30) {
         claused = Clause.getClause(str);
       } else {
         claused.add(str);
       }
       for (String s : claused) {
         List<Term> ner = MengNER.NER(s);
         if(ner.size()>0) {
           for(int i =0;i < ner.size()-1;i++) {
             res = res + ner.get(i).getRealName()+"/"+ner.get(i).getNatureStr() + " ";
           }
           res += ner.get(ner.size()-1);
         }
       }
       return Response.status(200).entity(res).build();
     }
     
}

