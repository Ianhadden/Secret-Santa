

public class Person {
    
    private final String name;
    private final String email;
    private final String likes;
    
    public Person(String name, String email, String likes){
        this.name = name;
        this.email = email;
        this.likes = likes;
    }
    
    public String getName(){
        return name;
    }
    
    public String getEmail(){
        return email;
    }
    
    public String getLikes(){
        return likes;
    }
    
    public boolean equals(Object o){
        if (!(o instanceof Person)){
            return false;
        } else {
            Person castedO = (Person) o;
            return (name.equals(castedO.name) && email.equals(castedO.email));
        }
    }
    
    @Override
    public int hashCode(){
        String temp = name + email;
        int hash = 0;
        for (int i = 0; i < temp.length(); i++){
            hash += 31 * temp.charAt(i);
        }
        return hash;
    }
}
