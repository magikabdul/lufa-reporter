package cloud.cholewa.reporter.raport.lufa.ai;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportPrompt {

    private static final String PROMPT =
        """
            1. Na bazie listy podanych opisów zadań przygotuj tylko jeden opis.
            2. Jeżeli nie zostały podane żadne opisy, zwróć `N\\A`
            3. Opis nie może być większy niż 500 znaków.
            4. Opis musi być w języku polskim.
            """;
}
