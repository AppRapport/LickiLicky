import java.util.*;
 
public class Rule
{
 
    public Rule()
    {
    }
 
    public String getFineRate(String offence, String cartype)
    {
        	ArrayList<Rule> ruleList = new ArrayList<Rule>();
          
          
          HashMap<String, String> offenceDescription = new HashMap<String, String>();
          offenceDescription.put("3","Parking other than in a parking lot.");
          offenceDescription.put("4","Using a parking place for unauthorised purpose.");
		  offenceDescription.put("10(1)","Parking in a parking place not designated for the use thereof. ");
          offenceDescription.put("11(1)","Drawing/Driving/Pushing/Parking motor vehicle in a disorderly manner without due regard for the safety of other vehicles and persons. ");
		  offenceDescription.put("11(2)","Pushing/Removing motor vehicle without the permission of owner or driver.");
		  offenceDescription.put("12(1)","Failure to obey signs exhibited in parking place. ");
		  offenceDescription.put("13","Parking beyond the boundaries of parking lot thereby causing obstruction. ");
		  offenceDescription.put("14(a)","Repairing/allowing repair to a motor vehicle in parking place. ");
		  offenceDescription.put("14(b)","Using motor vehicle within parking place for sale or promoting the sale of goods of any kind.");
		  offenceDescription.put("18(1)","Parking in a season parking area without possession of a valid season parking ticket. ");
		  offenceDescription.put("18(2)","Parking in season parking place without displaying the season parking ticket. ");
		  offenceDescription.put("18(3)","Displaying a season parking ticket that has alteration, erasure or other irregularity therein indicating that it has been tampered with. ");
		  offenceDescription.put("4(1)","Parking without displaying valid parking coupon(s) or displaying insufficient amount of parking coupons.");
		  offenceDescription.put("12","Displaying coupon that has alteration, erasure or other irregularity therein indicating that it has been tampered with.");
		  
          HashMap<String, String> offenceFineCycle = new HashMap<String, String>();
          offenceFineCycle.put("3","25");
          offenceFineCycle.put("4","80");
		  offenceFineCycle.put("10(1)","25");
		  offenceFineCycle.put("11(1)","25");
		  offenceFineCycle.put("11(2)","25");
		  offenceFineCycle.put("12(1)","25");
		  offenceFineCycle.put("13","25");
		  offenceFineCycle.put("14(a)","80");
		  offenceFineCycle.put("14(b)","80");
		  offenceFineCycle.put("18(1)","25");
		  offenceFineCycle.put("18(2)","25");
		  offenceFineCycle.put("18(3)","300");
		  offenceFineCycle.put("4(1)","8");
		  offenceFineCycle.put("12","80");
          
          HashMap<String, String> offenceFineCar = new HashMap<String, String>();
          offenceFineCar.put("3","50");
          offenceFineCar.put("4","80");
		  offenceFineCar.put("10(1)","50");
		  offenceFineCar.put("11(1)","50");
		  offenceFineCar.put("11(2)","50");
		  offenceFineCar.put("12(1)","50");
		  offenceFineCar.put("13","50");
		  offenceFineCar.put("14(a)","80");
		  offenceFineCar.put("14(b)","80");
		  offenceFineCar.put("18(1)","50");
		  offenceFineCar.put("18(2)","50");
		  offenceFineCar.put("18(3)","300");
		  offenceFineCar.put("4(1)","30");
		  offenceFineCar.put("12","80");
		  offenceFineCar.put("xx","xx");;
          
          HashMap<String, String> offenceFineHeavy = new HashMap<String, String>();
          offenceFineHeavy.put("3","80");
          offenceFineHeavy.put("4","80");
		  offenceFineHeavy.put("10(1)","80");
		  offenceFineHeavy.put("11(1)","80");
		  offenceFineHeavy.put("11(2)","80");
		  offenceFineHeavy.put("12(1)","80");
		  offenceFineHeavy.put("13","80");
		  offenceFineHeavy.put("14(a)","80");
		  offenceFineHeavy.put("14(b)","80");
		  offenceFineHeavy.put("18(1)","80");
		  offenceFineHeavy.put("18(2)","80");
		  offenceFineHeavy.put("18(3)","80");
		  offenceFineHeavy.put("4(1)","40");
		  offenceFineHeavy.put("12","80");
		  
          if (cartype.equalsIgnoreCase("Car")) {
            return offenceFineCar.get(offence) + "," + offenceDescription.get(offence);
          }          
          if (cartype.equalsIgnoreCase("Motorcycle")) {
            return offenceFineCycle.get(offence) + "," + offenceDescription.get(offence);
          }          
          if (cartype.equalsIgnoreCase("Heavyvehicle")) {
            return offenceFineHeavy.get(offence) + "," + offenceDescription.get(offence);
          }          
          return "0";

    }
}