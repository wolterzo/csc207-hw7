package ushahidiab;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Vector;

import edu.grinnell.glimmer.ushahidi.UshahidiCategory;
import edu.grinnell.glimmer.ushahidi.UshahidiClient;
import edu.grinnell.glimmer.ushahidi.UshahidiIncident;
import edu.grinnell.glimmer.ushahidi.UshahidiIncidentList;
import edu.grinnell.glimmer.ushahidi.UshahidiLocation;
import edu.grinnell.glimmer.ushahidi.UshahidiTestingClient;
import edu.grinnell.glimmer.ushahidi.UshahidiUtils;

public class UshahidiExtensions
{
  public static void printIncident(PrintWriter pen, UshahidiIncident incident)
  {
    pen.println("Incident #:" + incident.getTitle());
    pen.println(incident.getDescription());
    pen.println("location: " + incident.getLocation());
    pen.println("status: " + incident.getMode() + ", " + incident.getActive()
                + ", " + incident.getVerified());
    pen.println("Date: " + incident.getDate());
    pen.println();
  }

  public static UshahidiTestingClient testingClient()
  {
    UshahidiTestingClient client = new UshahidiTestingClient();
    for (int i = 0; i < 10; i++)
      {
        client.addIncident(UshahidiUtils.randomIncident());

      }
    return client;
  }

  public static void
    UshahidiRangeOfDates(UshahidiClient client, LocalDateTime begin,
                         LocalDateTime end)
      throws Exception
  {
    PrintWriter pen = new PrintWriter(System.out, true);
    UshahidiIncident temp = client.nextIncident();
    while (client.hasMoreIncidents())
      {
        if (begin.isBefore(temp.getDate()) && end.isAfter(temp.getDate()))
          {

            UshahidiExtensions.printIncident(pen, temp);
            temp = client.nextIncident();
          }//if
        else
          {
            temp = client.nextIncident();
          }//else
      }//while
  }//static UshahidiRangeOfDates

  public static Vector<UshahidiIncident>
    UshahidiVectorofDateRange(UshahidiClient client, LocalDateTime begin,
                              LocalDateTime end)
      throws Exception
  {
    Vector<UshahidiIncident> result = new Vector<UshahidiIncident>();
    UshahidiIncident temp = client.nextIncident();
    while (client.hasMoreIncidents())
      {
        if (begin.isBefore(temp.getDate()) && end.isAfter(temp.getDate()))
          {

            result.add(temp);
            temp = client.nextIncident();
          }//if
        else
          {
            temp = client.nextIncident();
          }//else
      }//while
    return result;

  }
}
