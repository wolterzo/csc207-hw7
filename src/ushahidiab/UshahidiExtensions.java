package ushahidiab;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Vector;
import java.util.function.Predicate;

import edu.grinnell.glimmer.ushahidi.UshahidiClient;
import edu.grinnell.glimmer.ushahidi.UshahidiIncident;
import edu.grinnell.glimmer.ushahidi.UshahidiTestingClient;
import edu.grinnell.glimmer.ushahidi.UshahidiUtils;
import edu.grinnell.glimmer.ushahidi.UshahidiWebClient;

public class UshahidiExtensions
{
  /**
   * Prints an UshahidiIncident
   * @param pen
   * @param incident
   */
  public static void printIncident(PrintWriter pen, UshahidiIncident incident)
  {
    pen.println("Incident #:" + incident.getTitle());
    pen.println(incident.getDescription());
    pen.println("location: " + incident.getLocation());
    pen.println("status: " + incident.getMode() + ", " + incident.getActive()
                + ", " + incident.getVerified());
    pen.println("Date: " + incident.getDate());
    pen.println();
  } // PrintIncident(PrintWriter, UshahidiIncident)

  /**
   * Prints a vector of UshahidiIncidents.
   * @param pen
   * @param incidents
   */
  public static void printVectorOfIncidents(PrintWriter pen,
                                            Vector<UshahidiIncident> incidents)
  {
    int length = incidents.size();
    for (int i = 0; i < length; i++)
      {
        printIncident(pen, incidents.get(i));
        pen.println();
      } // for
  } // PrintVectorOfIncicdents(PrintWriter, Vector)

  /**
   * Generates a sample Ushahidi client of ten random incidents.
   * @return
   */
  public static UshahidiTestingClient testingClient()
  {
    UshahidiTestingClient client = new UshahidiTestingClient();
    for (int i = 0; i < 10; i++)
      {
        client.addIncident(UshahidiUtils.randomIncident());

      } // for
    return client;
  } // UshahidiTestingClient()

  /**
   * 
   * @param client
   * @param begin
   * @param end
   * @throws Exception
   */
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

  /**
   * Builds and returns a vector of all the incidents in the client within the given 
   * time range.
   * @param client
   * @param begin
   * @param end
   * @return
   * @throws Exception
   */
  public static Vector<UshahidiIncident>
    UshahidiDateRangeFilter(UshahidiClient client, LocalDateTime begin,
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

  } // UshahidiDateRangeFilter(UshahidiClient, LocalDateTime, LocalDateTime)

  /**
   * given two sets of lat/long coordinates returns the distance between to two in 
   * kilometers
   * @param lat1
   * @param lon1
   * @param lat2
   * @param lon2
   * @return
   */
  public static double distanceKilometers(double lat1, double lon1,
                                          double lat2, double lon2)
  {
    double dlat = lat1 - lat2;
    double dlon = lon1 - lon2;
    double a =
        Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat2) * Math.cos(lat1)
            * Math.pow(Math.sin(dlon / 2), 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return 6373 * c;
  } // distanceKilometers

  /**
   * Builds and returns a vector of all the incidents in the client within a certain
   * distance of a location (latitude and longitude).
   * @param incidents
   * @param latitude
   * @param longitude
   * @param distance
   * @return
   */
  public static Vector<UshahidiIncident>
    UshahidiLocationFilter(Vector<UshahidiIncident> incidents, double latitude,
                           double longitude, double distance)
  {
    int length = incidents.size();
    Vector<UshahidiIncident> result = new Vector<UshahidiIncident>();
    for (int i = 0; i < length; i++)
      {
        UshahidiIncident currIncident = incidents.get(i);
        double d =
            distanceKilometers(currIncident.getLocation().getLatitude(),
                               currIncident.getLocation().getLongitude(),
                               latitude, longitude);
        if (d <= distance)
          {
            result.add(currIncident);
          } // if 
      } // for
    return result;
  } // UshahidiLocationFilter(Vector<UshahidiIncident>, double, double, double)

  /**
   * Returns a Vector of Ushahidi Incidents based on certain criteria.
   * @param incidents, Vector<UshahdiIncident>
   * @param pred, Predicate<UshahidiIncident>
   * @return Vector<UshahidiIncident>
   */
  public static Vector<UshahidiIncident>
    UshahidiVectorFilter(Vector<UshahidiIncident> incidents,
                         Predicate<UshahidiIncident> pred)
  {
    Vector<UshahidiIncident> result = new Vector<UshahidiIncident>();
    int length = incidents.size();
    for (int i = 0; i < length; i++)
      {
        UshahidiIncident currIncident = incidents.get(i);
        if (pred.test(currIncident))
          {
            result.add(currIncident);
          } // if
      } // for
    return result;
  } // UshahidiFilter(Vector, Predicate)

  public static
    Vector<UshahidiIncident>
    UshahidiClientFilter(UshahidiClient client, Predicate<UshahidiIncident> pred)
      throws Exception
  {
    Vector<UshahidiIncident> result = new Vector<UshahidiIncident>();
    UshahidiIncident temp = null;
    if (client.hasMoreIncidents())
      {
        temp = client.nextIncident();
      }
    while (client.hasMoreIncidents())
      {
        if (pred.test(temp))
          {
            result.add(temp);
            temp = client.nextIncident();
          } // if
        else
          {
            temp = client.nextIncident();
          } // else
      } // while
    return result;
  } // UshahidiClientFilter(UshahidiClient, Predicate<UshahidiIncident>)
} // UshahidiExtensions
