package ushahidiab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Vector;
import java.util.function.Predicate;

import edu.grinnell.glimmer.ushahidi.UshahidiIncident;
import edu.grinnell.glimmer.ushahidi.UshahidiWebClient;

/**
 * A TUI that implements three criteria for filtering, name, date and 
 * location and returns a vector of the incidents that meet that criteria.
 * @param PrintWriter pen
 * @param BufferedReader eyes
 * @param String prompt
 */
public class UshahidiTUI
{
  public static String readInput(PrintWriter pen, BufferedReader eyes,
                                 String prompt)
  {
    if (!prompt.equals(""))
      {
        pen.print(prompt);
        pen.flush();
      } // if there is a prompt
    try
      {
        return eyes.readLine();
      } // try
    catch (IOException e)
      {
        return "operator error";
      } // catch
  } // readInput(PrintWriter, BufferedReader, String)

  public static void runUshahidiTUI(PrintWriter pen, BufferedReader eyes)
    throws Exception
  {
    String webClientName =
        readInput(pen, eyes, "Please enter URL of Ushahidi client:");
    boolean quit = true;
    pen.println("OPTIONS");
    pen.println("Filter incidents in the following ways:");
    pen.println("date");
    pen.println("location");
    pen.println("name");
    String option = readInput(pen, eyes, "Type one of the above options:");
    Predicate<UshahidiIncident> pred = null;

    while (quit)
      {
        UshahidiWebClient webClient = new UshahidiWebClient(webClientName);
        switch (option)
          {
            case "date":
              int year1 =
                  Integer.valueOf(readInput(pen, eyes, "Enter start year:"));
              int month1 =
                  Integer.valueOf(readInput(pen, eyes, "Enter start month:"));
              int day1 =
                  Integer.valueOf(readInput(pen, eyes, "Enter start day:"));
              int year2 =
                  Integer.valueOf(readInput(pen, eyes, "Enter end year:"));
              int month2 =
                  Integer.valueOf(readInput(pen, eyes, "Enter end month:"));
              int day2 =
                  Integer.valueOf(readInput(pen, eyes, "Enter end day:"));
              LocalDateTime start = LocalDateTime.of(year1, month1, day1, 0, 0);
              LocalDateTime end = LocalDateTime.of(year2, month2, day2, 0, 0);
              pred =
                  (inc) ->
                    {
                      return (inc.getDate().isBefore(end) && inc.getDate()
                                                                .isAfter(start));
                    };
              break;
            case "location":
              double lat1 =
                  Double.valueOf(readInput(pen, eyes, "Enter latitude:"));
              double lon1 =
                  Double.valueOf(readInput(pen, eyes, "Enter longitude:"));
              double distance =
                  Double.valueOf(readInput(pen, eyes,
                                           "Enter distance (in Kilometers):"));
              pred =
                  (inc) ->
                    {
                      return (UshahidiExtensions.distanceKilometers(lat1,
                                                                    lon1,
                                                                    inc.getLocation()
                                                                       .getLatitude(),
                                                                    inc.getLocation()
                                                                       .getLongitude()) <= distance);
                    };
              break;
            case "name":
              String name =
                  readInput(
                            pen, eyes, "Enter a name or part of a name:").toLowerCase();
              pred = (inc) ->
                {
                  return inc.getTitle().toLowerCase().contains(name);
                };
              break;
            case "exit":
              quit = false;
              break;
            default:
              pen.println("Invalid option.");
          } // switch
        if (quit)
          {
            Vector<UshahidiIncident> incidents =
                UshahidiExtensions.UshahidiClientFilter(webClient, pred);
            UshahidiExtensions.printVectorOfIncidents(pen, incidents);
            option = readInput(pen, eyes, "Type one of the above options:");
          }
      } // while
    pen.close();
    eyes.close();
  } // runUshahidiTUI()

  public static void main(String[] args)
    throws Exception
  {
    PrintWriter pen = new PrintWriter(System.out, true);
    BufferedReader eyes = new BufferedReader(new InputStreamReader(System.in));

    runUshahidiTUI(pen, eyes);
  } // main
} // UshahidiTUI
