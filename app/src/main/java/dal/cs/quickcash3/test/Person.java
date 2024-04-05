package dal.cs.quickcash3.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class Person {
    private String firstName;
    private String lastName;
    private int age;

    public Person() {
        // Firebase needs an empty constructor.
    }

    public Person(@NonNull String firstName, @NonNull String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(firstName) + Objects.hashCode(lastName) + Objects.hashCode(age);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Person) {
            Person other = (Person) obj;
            return Objects.equals(firstName, other.firstName)
                && Objects.equals(lastName, other.lastName)
                && age == other.age;
        }
        return false;
    }

    public @Nullable String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NonNull String firstName) {
        this.firstName = firstName;
    }

    public @Nullable String getLastName() {
        return lastName;
    }

    public void setLastName(@NonNull String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
