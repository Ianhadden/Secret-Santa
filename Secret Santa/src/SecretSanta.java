import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SecretSanta {
    
    /**
     * All these constants can and should be customized to fit your needs
     */
    public static final String INPUTFILE = "secretsantafinal.tsv";
    public static final String CHEATSHEET = "cheatSheet.txt";
    public static final String EMAILS = "emails.txt";
    
    public static final String COSTLIMIT = "$15";
    public static final String EXCHANGELOCATION = "my house";
    public static final String EXCHANGEDATE = "December 25th, 2047";
    
    
    /**
     * Reads in a tsv file INPUTFILE to generate participants,
     * then assigns each participant a person to give a gift to, and outputs
     * this information along with email messages for each participant 
     * to CHEATSHEET and EMAILS.
     * 
     * @requires The file must be in tsv format with three Strings on each line:
     *           The first being a person's name, the second being their email,
     *           and the third being what they like (for gift ideas).
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException{
        ArrayList<Person> participants = generateParticipants(INPUTFILE);
        for (int i = 0; i < participants.size(); i++){
            System.out.println("Person " + (i + 1));
            System.out.println(participants.get(i).getName());
            System.out.println(participants.get(i).getEmail());
            System.out.println();
        }
        HashMap<Person, Person> pairs = generatePairs(participants);
        PrintStream cheatSheet = new PrintStream(new FileOutputStream(CHEATSHEET));
        PrintStream emails = new PrintStream(new FileOutputStream(EMAILS));
        for (Person p : pairs.keySet()){
            cheatSheet.println(p.getName() + " is giving to " + pairs.get(p).getName());
            emails.println(SecretSanta.emailMessage(p, pairs.get(p)));
        }
    }
    
    /**
     * Given a file name, forms and returns an ArrayList of people in that file.
     * @requires The file must be in tsv format with three Strings on each line:
     *           The first being a person's name, the second being their email,
     *           and the third being what they like (for gift ideas).
     * @return the ArrayList of Person formed
     * @throws IOException
     */
    public static ArrayList<Person> generateParticipants(String fileName) throws IOException{
        
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        ArrayList<Person> participants = new ArrayList<Person>();
        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
            String[] tokens = inputLine.split("\t");
            String name = tokens[0];
            String email = tokens[1];
            Person p;
            if (tokens.length == 3){ // if they've filled out their likes
                String likes = tokens[2];
                p = new Person(name, email, likes);
            } else { // if they haven't
                p = new Person(name, email, "");
            }
            participants.add(p);
        }
        return participants;
    }
    
    /**
     * Given an ArrayList of participants, returns a map from givers to receivers
     * 
     * @requires that there are no duplicates in the ArrayList and that the ArrayList has
     *           at least two Persons in it.
     * @param participants An ArrayList of person to create the pairs from
     * @return A HashMap from givers of gifts to their receivers
     */
    public static HashMap<Person, Person> generatePairs(ArrayList<Person> participants){
        ArrayList<Person> givers = new ArrayList<Person>();
        ArrayList<Person> receivers = new ArrayList<Person>();
        for (int i = 0; i < participants.size(); i++){
            givers.add(participants.get(i));
            receivers.add(participants.get(i));
        }
        HashMap<Person, Person> pairs = new HashMap<Person, Person>();
        Random randy = new Random();
        int i = givers.size();
        while (i > 2){
            int giverIndex = randy.nextInt(i);
            int receiverIndex = randy.nextInt(i);
            Person giver = givers.get(giverIndex);
            Person receiver = receivers.get(receiverIndex);
            if (!(giver.equals(receiver))){
                pairs.put(giver, receiver);
                givers.remove(giverIndex);
                receivers.remove(receiverIndex);
                i--;
            }
        }
        Person giver1 = givers.get(0);
        Person giver2 = givers.get(1);
        Person receiver1 = receivers.get(0);
        Person receiver2 = receivers.get(1);
        if (giver1.equals(receiver1) || giver2.equals(receiver2)){
            pairs.put(giver1, receiver2);
            pairs.put(giver2, receiver1);
        } else if (giver1.equals(receiver2) || giver2.equals(receiver1)){
            pairs.put(giver1, receiver1);
            pairs.put(giver2, receiver2);
        } else {
            int giverIndex = randy.nextInt(i);
            int recieverIndex = randy.nextInt(i);
            Person giver = givers.get(giverIndex);
            Person reciever = receivers.get(recieverIndex);
            pairs.put(giver, reciever);
            givers.remove(giverIndex);
            receivers.remove(recieverIndex);
            pairs.put(givers.get(0), receivers.get(0)); 
        }
        return pairs;
    }
    
    /**
     * Generates an email message based on a pairing of participants and the
     * exchange location, date, and cost limit.
     * 
     * @param sender
     * @param receiver
     * @return The email message
     */
    public static String emailMessage(Person sender, Person receiver){
        String msg = "To: " + sender.getEmail();
        msg += "\nHey there!";
        msg += "\nFor the Secret Santa, you'll be giving a gift to " + receiver.getName() + ".";
        if (receiver.getLikes().equals("")){
            msg += "\nIt seems they haven't put what they like on the spreadsheet, so if you don't";
            msg += " know them well you may have to do some sleuthing!";
        } else {
            msg += "\nThings they like include: " + receiver.getLikes() + ".";
        }
        msg += "\nThe gift exchange will be held at " + EXCHANGELOCATION + " on " + EXCHANGEDATE + "!";
        msg += " Please remember there is a " + COSTLIMIT + " limit.\n";
        msg += "Have fun, and Happy Holidays!";
        msg += "\n";
        return msg;
    }
}
