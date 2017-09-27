package net.stremo.starwarsjava.entities;

import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.CharConversionException;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "species")
public class Species extends BaseEntity {

    private String name;

    private String classification;
    private String designation;
    private String average_height;
    private String average_lifespan;
    private String hair_colors;
    private String skin_colors;

    private String eye_colors;
    private Long homeworld;
    private String language;
    private String created;
    private String edited;
//        extra table for saving    people ???

//    @ManyToMany(fetch = FetchType.EAGER)
//    private List<Character> characters = new ArrayList<>();

    @ManyToMany(mappedBy = "species")
    private List<Film> films = new ArrayList<>();

    //Is needed:
    public Species(){};

    public Species(String name, String classification, String designation, String average_height, String average_lifespan, String hair_colors, String skin_colors, String eye_colors, Long homeworld, String language, String created, String edited) {
        this.name = name;
        this.classification = classification;
        this.designation = designation;
        this.average_height = average_height;
        this.average_lifespan = average_lifespan;
        this.hair_colors = hair_colors;
        this.skin_colors = skin_colors;
        this.eye_colors = eye_colors;
        this.homeworld = homeworld;
        this.language = language;
        this.created = created;
        this.edited = edited;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getAverage_height() {
        return average_height;
    }

    public void setAverage_height(String average_height) {
        this.average_height = average_height;
    }

    public String getAverage_lifespan() {
        return average_lifespan;
    }

    public void setAverage_lifespan(String average_lifespan) {
        this.average_lifespan = average_lifespan;
    }

    public String getHair_colors() {
        return hair_colors;
    }

    public void setHair_colors(String hair_colors) {
        this.hair_colors = hair_colors;
    }

    public String getSkin_colors() {
        return skin_colors;
    }

    public void setSkin_colors(String skin_colors) {
        this.skin_colors = skin_colors;
    }

    public String getEye_colors() {
        return eye_colors;
    }

    public void setEye_colors(String eye_colors) {
        this.eye_colors = eye_colors;
    }

    public Long getHomeworld() {
        return homeworld;
    }

    public void setHomeworld(Long homeworld) {
        this.homeworld = homeworld;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getEdited() {
        return edited;
    }

    public void setEdited(String edited) {
        this.edited = edited;
    }

//    public List<Character> getCharacters() {
//        return characters;
//    }
//
//    public void setCharacters(List<Character> characters) {
//        this.characters = characters;
//    }
//
//    public void addCharacters(Character character) {
//        this.characters.add(character);
//    }

//    public String toString() {
//        return "Species{id: '" + getId() + "', name: '" + getName() + "', language: '" + getLanguage() + "'}";
//    }


}
