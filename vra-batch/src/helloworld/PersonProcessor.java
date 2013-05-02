package helloworld;

import org.springframework.batch.item.ItemProcessor;

public class PersonProcessor implements ItemProcessor<Personne, Personne> {

	public Personne process(final Personne personneInput) throws Exception {

		Personne personneOutput = null;

		// si la civilite a la valeur M la personne sera ecrite en base sinon on
		// la rejette

		if ("M".equals(personneInput.getCivilite())) {

			personneOutput = new Personne();

			personneOutput.setCivilite(personneInput.getCivilite());

			personneOutput.setId(personneInput.getId());

			personneOutput.setNom(personneInput.getNom());

			personneOutput.setPrenom(personneInput.getPrenom());

		}

		return personneOutput;

	}
}