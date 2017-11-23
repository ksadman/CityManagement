package com.samgauck;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A singleton class that manages all commands.
 */
public class Command {
    private static Command command;
    private final ArrayList<String> commands = new ArrayList<>(Arrays.asList("buy", "sell", "construct", "quit", "load", "save", "help"));
    private Economy economy = Economy.getInstance();
    private ArrayList<String> items = economy.getItems();
    private Command() {}

    /**
     * Gets the Command instance if it exists, otherwise make a new one.
     * @return The one and only Command instance.
     */
    public static Command getInstance() {
        command = command == null ? new Command() : command;
        return command;
    }

    /**
     * A high-level function that takes a command and call the function that it is trying to do.
     * @param command The command given by the user.
     */
    public void execute(String command) {
        ArrayList<String> words = new ArrayList<String>();
        command = command.toLowerCase();
        words.addAll(Arrays.asList(command.split(" ")));
        String word1 = words.get(0);
        words.remove(0);
        switch (word1) {
            case "buy":
                buy(words);
                break;
            case "sell":
                sell(words);
                break;
            case "construct":
                construct(words);
                break;
            case "quit":
                quit();
                break;
            case "load":
                load(words);
                break;
            case "save":
                save();
                break;
            case "help":
                help(words);
                break;
            default:
                System.out.println("Error 1.1: Command not recognized");
                return;
        }
    }

    /**
     * A function that buys items.
     * @param followingWords The user's command, minus the word "buy".
     */
    private void buy(ArrayList<String> followingWords) {
        System.out.println("");
        if (followingWords.size() == 0) {
            System.out.println("Error 1.2.1: No item to buy");
            return;
        }
        if (!items.contains(followingWords.get(0))) {
            System.out.println("Error 1.2.2: Item not recognized");
            return;
        }
        if (followingWords.get(1).matches("\\D+")) {
            System.out.println("Error 1.3: Not a valid amount");
            return;
        }
        String item = followingWords.get(0);
        int amount = Integer.parseInt(followingWords.get(1));
        if ((Main.getCity(0).resources.getMoney() - (economy.getPrice(item) * amount)) < 0) {
            if (followingWords.size() <= 3 || followingWords.get(2).contains("d")) {
                System.out.println("You don't have enough money to pay for " + amount + " " + item + " and you didn't specify that you could go into debt");
                System.out.println("The most you could buy is " + (int)Math.floor(Main.getCity(0).resources.getMoney()/economy.getPrice(item)));
                System.out.println("Transaction not completed");
                return;
            }
        }
        Main.getCity(0).resources.setMoney(Main.getCity(0).resources.getMoney() - (economy.getPrice(item) * amount));
        Main.getCity(0).resources.setItem(item, Main.getCity(0).resources.getItem(item) + amount);
        System.out.println("Transaction completed");
        System.out.println(Main.getCity(0).resources.toString());
    }

    /**
     * A function that sells items.
     * @param followingWords The user's command, minus the word "sell".
     */
    private void sell(ArrayList<String> followingWords) {
        System.out.println("");
        if (followingWords.size() == 0) {
            System.out.println("Error 1.2.1: No item to sell");
            return;
        }
        if (!items.contains(followingWords.get(0))) {
            System.out.println("Error 1.2.2: Item not recognized");
            return;
        }
        if (followingWords.get(1).matches("\\D+")) {
            System.out.println("Error 1.3: Not a valid amount");
            return;
        }
        String item = followingWords.get(0);
        int amount = Integer.parseInt(followingWords.get(1));
        if ((Main.getCity(0).resources.getItem(item) - amount) < 0) {
            System.out.println("You don't have enough " + item);
            System.out.println("The most you could sell is " + Main.getCity(0).resources.getItem(item));
            System.out.println("Transaction not completed");
            return;
        }
        Main.getCity(0).resources.setMoney(Main.getCity(0).resources.getMoney() + (economy.getPrice(item) * amount));
        Main.getCity(0).resources.setItem(item, Main.getCity(0).resources.getItem(item) - amount);
        System.out.println("Transaction completed");
        System.out.println(Main.getCity(0).resources.toString());
    }

    /**
     * A function that constructs items/things.
     * @param followingWords The user's command, minus the word "construct".
     */
    private void construct(ArrayList<String> followingWords) {
        //TODO: Construct
    }

    /**
     * Ends the looping command input.
     */
    private void quit() {
        Main.setLooping(false);
        System.out.println("Thanks for playing!");
        System.exit(0);
    }
    private void load(ArrayList<String> followingWords) {
        if (followingWords.size() <= 0) {
            System.out.println("Error 1.2: No save code given.");
        }else if (followingWords.size() > 1){
            System.out.println("Error 1.3: To load, execute \"load [save-code]\", but replace \"[save-code]\" with your save code.");
        }else {
            /* General checking start*/
            ArrayList<String> code = new ArrayList<>();
            code.addAll(Arrays.asList(followingWords.get(0).split("•")));
            if (code.size() != 4) {
                System.out.println("Error 1.2.1: Save code not valid");
                return;
            }
            /*General checking end*/
            /*Name checking start*/
            //No checking; anything is accepted
            /*Name checking end*/
            /*Resource checking start*/
            ArrayList<String> resources = new ArrayList<>();
            ArrayList<Integer> resourceInts = new ArrayList<>();
            resources.addAll(Arrays.asList(code.get(1).split("§")));
            for (int i = 0; i < resources.size(); i++) {
                try {
                    resourceInts.add(Integer.parseInt(resources.get(i)));
                }catch (NumberFormatException e) {
                    System.out.println("Error 1.2.3: Save code not valid");
                }
            }
            if (resourceInts.size() != Resources.getNumberOfResources() || resources.size() != Resources.getNumberOfResources()) { //If there is the wrong number of resources
                System.out.println("Error 1.2.3: Save code not valid");
                return;
            }
            /*Resource checking end*/
            /*Person checking starts*/
            ArrayList<String> people = new ArrayList<>();
            ArrayList<String> person = new ArrayList<>();
            people.addAll(Arrays.asList(code.get(2).split("§")));
            for (int i = 0; i < people.size(); i++) {
                person.addAll(Arrays.asList(people.get(i).split("‡")));
                if (person.size() != 5) { //wrong number of fields = 1.2.4
                    System.out.println("Error 1.2.4: Save code not valid");
                    return;
                }
                try {
                    Integer.parseInt(person.get(2));
                }catch (NumberFormatException e) {
                    System.out.println("Error 1.2.4.3: Save code not valid");
                    return;
                }
                try {
                    Profession.valueOf(person.get(3).toUpperCase());
                }catch (Exception e) {
                    System.out.println("Error 1.2.4.4: Save code not valid");
                    return;
                }
                person.clear();
            }
            /*Person checking ends*/
            /*Date checking starts*/
            ArrayList<String> date = new ArrayList<>();
            ArrayList<Integer> dateInts = new ArrayList<>();
            date.addAll(Arrays.asList(code.get(3).split("§")));
            if (date.size() != 3) { //Failure checking for changing the date
                System.out.println("Error 1.2.5: Save code not valid");
                return;
            }
            for (int i = 0; i < date.size(); i++) {
                try {
                    dateInts.add(Integer.parseInt(date.get(i)));
                }catch (NumberFormatException e) {
                    System.out.println("Error 1.2.5: Save code not valid");
                }
            }
            /*Date checking ends*/

            //Set city name. No reason this would fail.
            Main.getCity(0).setName(code.get(0));

            //Sets number of resources. Can fail if they don't pass Integers
            Main.getCity(0).resources.setAll(resourceInts.get(0), resourceInts.get(1), resourceInts.get(2), resourceInts.get(3), resourceInts.get(4), resourceInts.get(5), resourceInts.get(6), resourceInts.get(7), resourceInts.get(8), resourceInts.get(9));

            //Sets people. Can fail if they mess with the fields
            for (int i = 0; i < people.size(); i++) {
                person.addAll(Arrays.asList(people.get(i).split("‡")));
                Main.getCity(0).getCitizens().get(i).setFirstName(person.get(0)); //If incorrect(impossible) = Error 1.2.4.1
                Main.getCity(0).getCitizens().get(i).setLastName(person.get(1)); //If incorrect(impossible) = Error 1.2.4.2
                Main.getCity(0).getCitizens().get(i).setAge(Integer.parseInt(person.get(2))); //If incorrect = Error 1.2.4.3
                Main.getCity(0).getCitizens().get(i).setJob(Profession.valueOf(person.get(3).toUpperCase())); //If incorrect = Error 1.2.4.4
                Main.getCity(0).getCitizens().get(i).setSex((person.get(4).equalsIgnoreCase("m")) ? Sex.MALE : Sex.FEMALE); //If incorrect(impossible) = Error 1.2.4.5
                person.clear();
            }

            //Sets date. Can fail if they don't pass Integers
            Main.getDate().setDate(dateInts.get(0), dateInts.get(1), dateInts.get(2));

            System.out.println("Loaded!");
        }
    }
    private void save() {
        StringBuilder s = new StringBuilder();
        s.append(Main.getCity(0).getName()).append("•");
        s.append(Main.getCity(0).resources.simpleString()).append("•");
        Main.getCity(0).getCitizens().forEach(p -> {
            s.append(p.getFirstName()).append("‡");
            s.append(p.getLastName()).append("‡");
            s.append(p.getAge()).append("‡");
            s.append(p.getJob()).append("‡");
            s.append(p.getSex().toString().substring(0,1)).append("§");
        });
        s.reverse().deleteCharAt(0).reverse().append("•"); //deletes the last "§" form the getCitizens().foreach loop and adds the •
        s.append(Main.getDate().simpleString()).append("•");
        //TODO: items.forEach() (or other way) of printing out the current prices & LOAD IT
        /*
        s.reverse().deleteCharAt(0).reverse(); //deletes the last "§" form the items.foreach loop
        */
        System.out.println("Your save code is:");
        System.out.println(s.toString());
    }
    private void help(ArrayList<String> followingWords) {
        if (followingWords.size() == 0) {
            StringBuilder s = new StringBuilder();
            s.append("A proper command starts with a command word. The list of command words is as follows").append("\n");
            for (int i = 0; i < commands.size(); i++) {
                s.append(commands.get(i)).append(", ");
            }
            s.reverse().deleteCharAt(0).deleteCharAt(0).reverse().append("\n");
            s.append("\n");
            s.append("Buy, sell, construct, and load all require additional arguments").append("\n");
            s.append("Help can be called with or without arguments").append("\n");
            s.append("Quit and save ignore arguments").append("\n");
            s.append("Buy, sell, and construct accept an item (to buy, sell, or construct) and an amount. For example:").append("\n");
            s.append("buy food 100").append("\n");
            s.append("will buy 100 food (if you have enough money)").append("\n");
            s.append("\n");
            s.append("Buy, specifically, has extra options. You can add a \"d\" flag as an extra argument to let yourself go into debt").append("\n");
            s.append("Help can be called by itself (as you have), or with a command to learn more about that command").append("\n");
            s.append("Quit and save can be passed arguments, but they will ignore them completely").append("\n");
            System.out.println(s.toString());
            return;
        }
    }
}
