package ushahidiab;

import java.io.PrintWriter;

import edu.grinnell.glimmer.ushahidi.UshahidiLocation;
import edu.grinnell.glimmer.ushahidi.UshahidiClient;
import edu.grinnell.glimmer.ushahidi.UshahidiIncident;
import edu.grinnell.glimmer.ushahidi.UshahidiTestingClient;
import edu.grinnell.glimmer.ushahidi.UshahidiUtils;
import edu.grinnell.glimmer.ushahidi.UshahidiWebClient;
import edu.grinnell.glimmer.ushahidi.UshahidiCategory;

import java.time.LocalDateTime;
import java.util.Vector;

public class PrintIncidentExperiment
{

  public static void main(String[] args)
    throws Exception
  {
    // Create the output device
    PrintWriter pen = new PrintWriter(System.out, true);
    UshahidiLocation location = new UshahidiLocation(3, "what", 45.0, 34.0);
    UshahidiCategory category = new UshahidiCategory(2, "crazy");
    String description = new String("holy cow look at that");
    // A few basic incidents
    // UshahidiExtensions.printIncident(pen, UshahidiUtils.SAMPLE_INCIDENT);
    /*   UshahidiExtensions.printIncident(pen, UshahidiUtils.randomIncident());
       UshahidiExtensions.printIncident(pen, UshahidiUtils.randomIncident());
       UshahidiExtensions.printIncident(pen, UshahidiUtils.randomIncident());
       UshahidiExtensions.printIncident(pen, UshahidiUtils.randomIncident()); */

    // A newly created incident
    //     UshahidiIncident myIncident = new UshahidiIncident(1,description, LocalDateTime.now(), location, "dude did you see that over there?", category);
    UshahidiIncident myIncident = new UshahidiIncident();
    //UshahidiExtensions.printIncident(pen, myIncident);

    // One from a list
    UshahidiClient client = UshahidiUtils.SAMPLE_CLIENT;
    // UshahidiExtensions.printIncident(pen, client.nextIncident());

    // One that requires connecting to the server
    UshahidiClient webclient =
        new UshahidiWebClient("http://ushahidi1.grinnell.edu/sandbox/");
    // UshahidiExtensions.printIncident(pen, webclient.nextIncident());
 /*   Vector<UshahidiIncident> incidents = 
        UshahidiExtensions.UshahidiDateRangeFilter(webclient, LocalDateTime.of(2014, 9, 1, 0, 0), LocalDateTime.of(2014, 10, 1, 0, 0));*/
    //UshahidiRangeofDates
     UshahidiExtensions.UshahidiRangeOfDates(UshahidiExtensions.testingClient(),
                                             LocalDateTime.of(2012, 1, 1, 0, 0),
                                             LocalDateTime.of(2012, 1, 1, 1, 0));

     pen.println("---------------------webclient-------------");

    UshahidiExtensions.UshahidiRangeOfDates(webclient,
                                             LocalDateTime.of(2014, 9, 1, 0, 0),
                                             LocalDateTime.of(2014, 10, 1, 0, 0));
                                            
    /*
    UshahidiExtensions.printVectorOfIncidents(pen,
                                              (UshahidiExtensions.UshahidiDateRangeFilter(webclient,
                                                                                          LocalDateTime.of(2014,
                                                                                                           9,
                                                                                                           1,
                                                                                                           0,
                                                                                                           0),
                                                                                          LocalDateTime.of(2014,
                                                                                                           10,
                                                                                                           1,
                                                                                                           0,
                                                                                                           0))));
  */
    UshahidiExtensions.printVectorOfIncidents(pen, UshahidiExtensions.UshahidiClientFilter(webclient, (UshahidiIncident inc) -> 
      { return inc.getTitle().equals("ZE FRENCH NSA"); }
    ));
  } // main(String[])
}
