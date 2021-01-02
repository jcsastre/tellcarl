package com.tellcarl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class TellCarlApplication
{
	public static void main(String[] args) {
		SpringApplication.run(TellCarlApplication.class, args);
	}

//	public static void main(String[] args) throws IOException
//	{
//		// Instantiates a client
//		try ( LanguageServiceClient language = LanguageServiceClient.create()) {
//
//			// The text to analyze
//			String text = "Estoy con unos amigos de la ZRB, y también está Quim. Nos metemos en un bar alargado y abarrotado de gente. Está en la ciudad de León. Es de noche. Nos perdemo por dentro del bar. Yo me agobio con tanta gente. Luego busco a Quim. Está en el fondo del bar, cerca de la tele. El bar se va vaciando, quedan los clientes habituales. Quim se acerca a un rincón donde venden chucherías (es como una habitación aparta, aunque abierta). Luego salimos de esa zona, mis amigos de la ZRB se han ido. Le pregunto a una camarera vestida de rojo si sabe donde va la gente de marcha después de pasar por este bar. Ella me dice que cuanto le puedo pagar por esa información. Yo le digo que le pago con el hecho de que un niño encuentre a su madre (Zaida, la madre de Quim). Entonces me dá la información. Estamos un rato sentados juntos. Ella es joven y estudiante. Yo acaricio la pierna por debajo de la rodilla de Quim. Nos quedamos mirando a Quim. Luego nos despedimos cariñosamente.";
//			Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();
//
//			final AnalyzeEntitiesResponse entities = language.analyzeEntities(doc);
//
//			final AnalyzeSyntaxResponse analyzeSyntaxResponse = language.analyzeSyntax(doc);
//
//			// Detects the sentiment of the text
//			Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();
//
//			System.out.printf("Text: %s%n", text);
//			System.out.printf("Sentiment: %s, %s%n", sentiment.getScore(), sentiment.getMagnitude());
//		}
//
//	}
}
