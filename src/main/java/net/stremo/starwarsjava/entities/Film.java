package net.stremo.starwarsjava.entities;

import org.h2.table.Plan;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "films")
public class Film extends BaseEntity{

    private String title;

    private Long episode_id;

    @Lob
    private String opening_crawl;

    private String director;

    private String producer;

    private String release_date;

    private String created;

    private String edited;

    @ManyToMany(mappedBy = "films")
    private List<Character> characters = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Planet> planets = new ArrayList<>();

    @ManyToMany//(fetch = FetchType.EAGER)
    private List<Species> species = new ArrayList<>();

    @ManyToMany//(fetch = FetchType.EAGER)
    private List<Starship> starships = new ArrayList<>();

    @ManyToMany//(fetch = FetchType.EAGER)
    private List<Vehicle> vehicles = new ArrayList<>();

    public Film () {}

    public Film(String title, Long episode_id, String opening_crawl, String director, String producer, String release_date, String created, String edited) {
        this.title = title;
        this.episode_id = episode_id;
        this.opening_crawl = opening_crawl;
        this.director = director;
        this.producer = producer;
        this.release_date = release_date;
        this.created = created;
        this.edited = edited;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getEpisode_id() {
        return episode_id;
    }

    public void setEpisode_id(Long episode_id) {
        this.episode_id = episode_id;
    }

    public String getOpening_crawl() {
        return opening_crawl;
    }

    public void setOpening_crawl(String opening_crawl) {
        this.opening_crawl = opening_crawl;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
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


    public List<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }

    public void addCharacter(Character character) {
        if (character != null) {
            this.characters.add(character);
            character.addFilm(this);
        }
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public void setPlanets(List<Planet> planets) {
        this.planets = planets;
    }

    public void addPlanet(Planet planet) {
        this.planets.add(planet);
    }


    public List<Species> getSpecies() {
        return species;
    }

    public void setSpecies(List<Species> species) {
        this.species = species;
    }

    public void addSpecies(Species species){
        this.species.add(species);
    }


    public List<Starship> getStarships() {
        return starships;
    }

    public void setStarships(List<Starship> starships) {
        this.starships = starships;
    }

    public void addStarship (Starship starship) {
        this.starships.add(starship);
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public void addVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);
    }
}
