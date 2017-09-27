package net.stremo.starwarsjava;

import com.fasterxml.jackson.databind.ser.Serializers;
import net.stremo.starwarsjava.entities.*;
import net.stremo.starwarsjava.entities.Character;
import net.stremo.starwarsjava.utils.FileReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
//import java.lang.Character;
import java.util.stream.Stream;

@Service
public class TestDataSetup {

    @Autowired
    FilmRepository filmRepository;


    @Autowired
    SpeciesRepository speciesRepository;

    @Autowired
    PlanetsRepository planetRepository;

    @Autowired
    StarshipsRepository starshipsRepository;

    @Autowired
    CharactersRepository charactersRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Transactional
    public void run() {

        fillContent("films.json", filmRepository, "net.stremo.starwarsjava.entities.Film");
        fillContent("people.json", charactersRepository, "net.stremo.starwarsjava.entities.Character");
        fillContent("species.json", speciesRepository, "net.stremo.starwarsjava.entities.Species");
        fillContent("planets.json", planetRepository, "net.stremo.starwarsjava.entities.Planet");
        fillContent("starships.json", starshipsRepository, "net.stremo.starwarsjava.entities.Starship");
        fillContent("vehicles.json", vehicleRepository, "net.stremo.starwarsjava.entities.Vehicle");

        System.out.println("Connect Film with other entities.");
        connectFilmEntities("films.json", filmRepository, charactersRepository, "characters");

        System.out.println(filmRepository.findOne(1L).toString());

    }


    /**
     * mind fuck! Nothing else
     * @param filename
     * @param repository
     * @param className
     */
    private void fillContent (String filename, PagingAndSortingRepository repository, String className) {
        try {
            new JSONArray(FileReader.readFileContent(filename)).forEach(item -> {
                try {
                    BaseEntity entity = (BaseEntity) Class.forName(className).newInstance();
                    entity.setDataFromJSON(((JSONObject) item).getJSONObject("fields"));
                    repository.save(entity);
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(repository.count() + " " + StringUtils.capitalize(filename).replace(".json", "") + " loaded");
    }


    private void connectFilmEntities (String filename, PagingAndSortingRepository<Film, Long> repository, PagingAndSortingRepository<Character, Long> aimRepository, String fieldname) {
        JSONArray array = new JSONArray(FileReader.readFileContent(filename));
        for (int index = 0; index <  array.length(); index++ ) {
            JSONObject o = array.getJSONObject(index);
            System.out.println(repository.getClass().getSimpleName());
            Film e = repository.findOne(index + 1L);
            JSONObject oContent = o.getJSONObject("fields");
            if (oContent.has(fieldname)) {
                JSONArray connections = oContent.getJSONArray(fieldname);
                for (int conI = 0; conI < connections.length(); conI++  ) {
                    Long id = connections.getLong(conI);
                    e.addCharacter(aimRepository.findOne(id));
                }
            }

        }
    }
}
