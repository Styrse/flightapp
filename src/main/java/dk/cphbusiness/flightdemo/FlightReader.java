package dk.cphbusiness.flightdemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.cphbusiness.flightdemo.dtos.FlightDTO;
import dk.cphbusiness.flightdemo.dtos.FlightInfoDTO;
import dk.cphbusiness.utils.Utils;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Purpose:
 *
 * @author: Thomas Hartmann
 */
public class FlightReader {

    public static void main(String[] args) {
        try {
            //List<FlightDTO> flightList = getFlightsFromFile("flights.json");
            //List<FlightInfoDTO> flightInfoDTOList = getFlightInfoDetails(flightList);
            //flightInfoDTOList.forEach(System.out::println);
            //System.out.println(getAvgFlightTimeForAirline("Air Explore"));
            //System.out.println(getMinFlightTimeForAirline("Jet Linx Aviation"));
            //System.out.println(getMaxFlightTimeForAirline("Nordwind Airlines"));
            System.out.println(getAverageFlightTimeBetweenAirports("Pulkovo", "Kurumoch"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<FlightDTO> getFlightsFromFile(String filename) throws IOException {

        ObjectMapper objectMapper = Utils.getObjectMapper();

        // Deserialize JSON from a file into FlightDTO[]
        FlightDTO[] flightsArray = objectMapper.readValue(Paths.get("flights.json").toFile(), FlightDTO[].class);

        // Convert to a list
        List<FlightDTO> flightsList = List.of(flightsArray);
        return flightsList;
    }

    public static List<FlightInfoDTO> getFlightInfoDetails(List<FlightDTO> flightList) {
        List<FlightInfoDTO> flightInfoList = flightList.stream()
           .map(flight -> {
                LocalDateTime departure = flight.getDeparture().getScheduled();
                LocalDateTime arrival = flight.getArrival().getScheduled();
                Duration duration = Duration.between(departure, arrival);
                FlightInfoDTO flightInfo =
                        FlightInfoDTO.builder()
                            .name(flight.getFlight().getNumber())
                            .iata(flight.getFlight().getIata())
                            .airline(flight.getAirline().getName())
                            .duration(duration)
                            .departure(departure)
                            .arrival(arrival)
                            .origin(flight.getDeparture().getAirport())
                            .destination(flight.getArrival().getAirport())
                            .build();

                return flightInfo;
            })
        .toList();
        return flightInfoList;
    }

    public static double getAvgFlightTimeForAirline(String airlineName) throws IOException {
        List<FlightDTO> flightList = FlightReader.getFlightsFromFile("flights json");
        List<FlightInfoDTO> flightInfoDTOList = FlightReader.getFlightInfoDetails(flightList);

        double avgTime = flightInfoDTOList.stream()
                .filter(flight -> flight.getAirline() != null)
                .filter(flight -> flight.getAirline().equals(airlineName))
                .mapToDouble(flight -> flight.getDuration().toMinutes())
                .average()
                .getAsDouble();

        return avgTime;
    }

    public static double getMinFlightTimeForAirline(String airlineName) throws IOException {
        List<FlightDTO> flightList = FlightReader.getFlightsFromFile("flights json");
        List<FlightInfoDTO> flightInfoDTOList = FlightReader.getFlightInfoDetails(flightList);

        double minFlightTime = flightInfoDTOList.stream()
                .filter(flight -> flight.getAirline() != null)
                .filter(flight -> flight.getAirline().equals(airlineName))
                .mapToDouble(flight -> flight.getDuration().toMinutes())
                .min()
                .getAsDouble();

        return minFlightTime;
    }

    public static double getMaxFlightTimeForAirline(String airlineName) throws IOException {
        List<FlightDTO> flightList = FlightReader.getFlightsFromFile("flights json");
        List<FlightInfoDTO> flightInfoDTOList = FlightReader.getFlightInfoDetails(flightList);

        double maxFlightTime = flightInfoDTOList.stream()
                .filter(flight -> flight.getAirline() != null)
                .filter(flight -> flight.getAirline().equals(airlineName))
                .mapToDouble(flight -> flight.getDuration().toMinutes())
                .max()
                .getAsDouble();

        return maxFlightTime;
    }

    public static double getAverageFlightTimeBetweenAirports(String airportA, String airportB) throws IOException {
        List<FlightDTO> flightList = FlightReader.getFlightsFromFile("flights json");
        List<FlightInfoDTO> flightInfoDTOList = FlightReader.getFlightInfoDetails(flightList);

        double avgFlightTime = flightInfoDTOList.stream()
                .filter(flight -> flight.getOrigin() != null)
                .filter(flight -> flight.getOrigin().equals(airportA))
                .filter(flight -> flight.getDestination().equals(airportB))
                .mapToDouble(flight -> flight.getDuration().toMinutes())
                .average()
                .getAsDouble();

        return avgFlightTime;
    }
}
