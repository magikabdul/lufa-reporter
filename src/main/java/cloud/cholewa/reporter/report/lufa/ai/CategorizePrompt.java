package cloud.cholewa.reporter.report.lufa.ai;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategorizePrompt {

    public static final String PROMPT = """
        Wytyczne jak określać typ zadania.
        
        Dostępne są następujące kategorie zadań:
        1.  Nazwa: SOFTWARE_DEVELOPMENT,
            Opis: Projektowanie i testowanie oprogramowania w odgórnie ustalonym czasie zgodnie wymaganiami biznesowymi.
        2.  Nazwa: CONSULTING_AND_TRAINING,
            Opis: Udzielenie konsultacji oraz przeprowadzanie szkoleń w zakresie wytwarzania oprogramowania.
        3.  Nazwa: DOCUMENTATION,
            Opis: Przygotowywanie dokumentacji technicznej i projektowej związanej z wytwarzanym oprogramowaniem w formie i na zasadach obowiązujących u Zlecającego.
        4.  Nazwa: CODE_ANALYSIS_AND_REFINEMENT,
            Opis: Analiza wymagań, tworzenie i doskonalenie kodu źródłowego i weryfikacja powstających funkcjonalności.
        5.  Nazwa: BUG_FIXING_AND_MAINTENANCE,
            Opis: Rozwiązywanie błędów i wprowadzanie zmian kodzie źródłowym.
        6.  Nazwa: TECHNOLOGY_SELECTION,
            Opis: Dobór technologii do rozwiązań na podstawie znajomości trendów rozwoju oprogramowania i różnych technologii.
        7.  Nazwa: ARCHITECTURE_DESIGN,
            Opis: Analiza i projektowanie architektury oprogramowania w zleconych projektach.
        
        Na podstawie opisu zadania dopasuj je do jednej z kategorii.
        Nie wymyślaj innych kategorii niż podane.
        
        Zwróć odpowiedź w formacie json zawierającym tylko:
        1. klucz "category" z wartością nazwy kategorii zadania.
        2. klucz "description" z opisem zadania.
        
        """;
}
