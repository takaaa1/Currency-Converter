package br.com.alura;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {

    private static final String API_KEY = "0d1896711078becb277b82ba";
    private static final String BASE_CODE = "USD";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/" + BASE_CODE;

    public static void main(String[] args) {
        try {
            boolean exit = false;
            while (!exit) {
                // GET CONVERTED RATES FROM API
                JsonObject rates = getExchangeRates();

                // MENU OPTIONS
                System.out.println("""
                
                ********************************************************
                CURRENCY CONVERTER
                
                SELECT A OPTION FROM BELOW:
                
                1- USD to BRL
                2- USD to EUR
                3- USD to JPY
                4- USD to GBP
                5- USD to CHF
                6- USD to CNY
                7- LEAVE
                
                ********************************************************
                
                """);

                // READ USER INPUT
                int option = readOption();

                // CONVERT BASED ON USER SELECTION
                double amountValue;

                switch (option) {
                    case 1:
                        amountValue = getAmount("USD");
                        convertAndPrint(amountValue, "BRL", rates);
                        break;

                    case 2:
                        amountValue = getAmount("USD");
                        convertAndPrint(amountValue, "EUR", rates);
                        break;

                    case 3:
                        amountValue = getAmount("USD");
                        convertAndPrint(amountValue, "JPY", rates);
                        break;

                    case 4:
                        amountValue = getAmount("USD");
                        convertAndPrint(amountValue, "GBP", rates);
                        break;

                    case 5:
                        amountValue = getAmount("USD");
                        convertAndPrint(amountValue, "CHF", rates);
                        break;

                    case 6:
                        amountValue = getAmount("USD");
                        convertAndPrint(amountValue, "CNY", rates);
                        break;

                    case 7:
                        exit = true;
                        break;

                    default:
                        System.out.println("Invalid option.");
                }
            }
            System.out.println("Thanks for using the Currency Converter!");
        } catch (IOException e) {
            System.out.println("User input error: " + e.getMessage());
        }
    }

    // METHOD TO GET CONVERSION RATES FROM API
    private static JsonObject getExchangeRates() throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Parse the JSON response string to a JsonObject
        JsonParser parser = new JsonParser();
        JsonObject jsonResponse = parser.parse(response.toString()).getAsJsonObject();
        return jsonResponse.getAsJsonObject("conversion_rates");
    }

    // METHOD TO READ THE SELECTED OPTION FROM USER
    private static int readOption() {
        int option = 0;
        boolean validInput = false;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (!validInput) {
            try {
                option = Integer.parseInt(reader.readLine());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Please type a valid number.");
            } catch (IOException e) {
                System.out.println("User input error: " + e.getMessage());
            }
        }

        return option;
    }

    // METHOD TO REQUEST AND OBTAIN THE VALUE OF THE SOURCE CURRENCY
    private static double getAmount(String currency) throws IOException {
        System.out.println("Type the amount in " + currency + " to convert: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return Double.parseDouble(reader.readLine());
    }

    // METHOD TO CONVERT AND PRINT THE CONVERSION RESULT
    private static void convertAndPrint(double amountValue, String targetCurrency, JsonObject rates) {
        if (rates.has(targetCurrency)) {
            double exchangeRate = rates.get(targetCurrency).getAsDouble();
            double convertedAmount = amountValue * exchangeRate;
            System.out.println("The converted amount is: " + convertedAmount + " " + targetCurrency);
        } else {
            System.out.println("Conversion rate for " + targetCurrency + " not available.");
        }
    }
}