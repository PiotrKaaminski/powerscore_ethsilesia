import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import LanguageDetector from 'i18next-browser-languagedetector';

const resources = {
  pl: {
    translation: {
      navbar: {
        client: 'Sekcja Klienta',
        employee: 'Sekcja Pracownika',
      },
      common: {
        back: 'Powrót',
        next: 'Dalej'
      },
      client: {
        checkVerification: {
          title: 'Sprawdź swoją weryfikację',
          documentType: 'Typ dokumentu',
          documentNumber: 'Numer dokumentu',
          submit: 'Sprawdź',
          exists: 'Weryfikacja dla podanych danych istnieje.',
          notExists: 'Nie znaleziono weryfikacji dla podanych danych.',
          pesel: 'PESEL',
          passport: 'Paszport',
          nip: 'NIP',
          docNumberRequired: 'Numer dokumentu jest wymagany',
          invalidPesel: 'Niepoprawny numer PESEL',
          invalidNip: 'Niepoprawny numer NIP'
        },
        personalInfo: {
          title: 'Dane osobowe',
          firstName: 'Imię',
          lastName: 'Nazwisko',
          birthdate: 'Data urodzenia',
          nationality: 'Narodowość',
          email: 'E-mail',
          phoneNumber: 'Numer telefonu',
          countryCode: 'Kierunkowy',
          submit: 'Zapisz dane',
          success: 'Dane zostały pomyślnie zapisane.',
          loading: 'Ładowanie weryfikacji...',
          error: 'Wystąpił błąd: {{message}}'
        },
        identityDocument: {
          title: 'Dokument tożsamości',
          type: 'Typ dokumentu',
          number: 'Numer dokumentu',
          image: 'Zdjęcie dokumentu',
          upload: 'Wgraj',
          selectFile: 'Wybierz plik',
          imageUploadedSuccess: 'Zdjęcie zostało wgrane pomyślnie',
          imageLoadError: 'Błąd podczas pobierania zdjęcia'
        },
        stepper: {
          kyc: 'Weryfikacja KYC',
          bank: 'Weryfikacja Bankowa',
          ai: 'Rekomendacja AI',
          finalSummary: 'Podsumowanie końcowe'
        },
        bankVerification: {
          title: 'Weryfikacja Bankowa',
          description: 'Pobranie Twoich danych z otwartej bankowości wymaga zgody. Prosimy o podjęcie decyzji.',
          approve: 'Zatwierdzam raport',
          reject: 'Odrzucam raport',
          success: 'Twoja decyzja została zapisana.',
          alreadyApproved: 'Raport bankowy został już zatwierdzony.',
          alreadyRejected: 'Raport bankowy został odrzucony.',
          waiting: 'Oczekiwanie na pobranie danych z banku...'
        },
        aiRecommendation: {
          title: 'Rekomendacja AI',
          description: 'Nasza sztuczna inteligencja przeanalizowała Twoje dane.',
          waiting: 'Trwa analiza danych przez AI...'
        },
        finalSummary: {
          title: 'Podsumowanie końcowe',
          description: 'Oto ostateczna decyzja w sprawie Twojej weryfikacji.',
          waiting: 'Oczekiwanie na ostateczną decyzję...'
        }
      },
      employee: {
        createVerification: {
          title: 'Utwórz nową weryfikację',
          documentType: 'Typ dokumentu',
          documentNumber: 'Numer dokumentu',
          submit: 'Rozpocznij weryfikację',
          success: 'Weryfikacja została pomyślnie utworzona. ID: {{id}}',
          error: 'Błąd: {{message}}',
          pesel: 'PESEL',
          passport: 'Paszport',
          nip: 'NIP',
          docNumberRequired: 'Numer dokumentu jest wymagany',
          invalidPesel: 'Niepoprawny numer PESEL',
          invalidNip: 'Niepoprawny numer NIP'
        },
        verificationList: {
          title: 'Lista weryfikacji',
          id: 'ID',
          status: 'Status',
          client: 'Klient',
          document: 'Dokument',
          startDate: 'Data rozpoczęcia',
          finishDate: 'Data zakończenia',
          actions: 'Akcje',
          statuses: {
            IN_PROGRESS: 'W toku',
            VERIFIED: 'Zweryfikowany',
            AWAITING_FINAL_DECISION: 'Oczekiwanie na decyzję',
            FAILED: 'Niepowodzenie'
          },
          noData: 'Brak weryfikacji do wyświetlenia',
          rowsPerPage: 'Wierszy na stronę:',
          search: 'Szukaj...',
          refresh: 'Odśwież',
          filters: {
            title: 'Filtry',
            clear: 'Wyczyść filtry',
            firstName: 'Imię',
            lastName: 'Nazwisko',
            documentNumber: 'Numer dokumentu',
            documentType: 'Typ dokumentu',
            nationality: 'Narodowość',
            email: 'E-mail',
            phoneNumber: 'Numer telefonu',
            startDateFrom: 'Data rozpoczęcia od',
            startDateTo: 'Data rozpoczęcia do',
            status: 'Status'
          }
        },
        verificationDetails: {
          title: 'Szczegóły weryfikacji',
          noClientData: 'Klient nie uzupełnił jeszcze danych osobowych',
          noImage: 'Zdjęcie dokumentu nie zostało jeszcze przesłane',
          steps: {
            kyc: 'Weryfikacja KYC',
            bank: 'Weryfikacja Bankowa',
            ai: 'Rekomendacja AI',
            finalSummary: 'Podsumowanie końcowe',
            status: 'Status',
            statuses: {
              WAITING_FOR_PREVIOUS: 'Oczekiwanie',
              IN_PROGRESS: 'W toku',
              FINISHED: 'Zakończono'
            }
          },
          bank: {
            title: 'Weryfikacja Bankowa',
            approval: 'Zgoda klienta',
            approved: 'Zatwierdzono',
            rejected: 'Odrzucono',
            notApproved: 'Nie udzielono zgody na weryfikację bankową',
            noReport: 'Raport bankowy nie jest jeszcze dostępny',
            reportPrepared: 'Użytkownik jeszcze się nie uwierzytelnił w banku',
            reportInProgress: 'Pobieranie raportu jeszcze się nie skończyło',
            reportData: 'Dane raportu:',
            owners: 'Właściciele',
            accounts: 'Konta'
          },
          ai: {
            title: 'Rekomendacja AI',
            noRecommendation: 'Rekomendacja AI nie jest jeszcze dostępna',
            recommendationInProgress: 'Generowanie rekomendacji jeszcze się nie skończyło',
            recommendationData: 'Treść rekomendacji:'
          },
          final: {
            title: 'Decyzja Końcowa',
            noDecision: 'Decyzja końcowa nie jest jeszcze dostępna',
            decisionContent: 'Treść decyzji:',
            error: 'Wystąpił błąd',
            decision: 'Decyzja',
            accepted: 'Zaakceptowana',
            rejected: 'Odrzucona',
            isAccepted: 'Wynik weryfikacji',
            agreementType: 'Typ umowy',
            agreementNormal: 'Normalna',
            agreementPrepaid: 'Pre-paid',
            placeholder: 'Wpisz uzasadnienie decyzji...',
            submit: 'Wyślij decyzję'
          }
        }
      },
      theme: {
        light: 'Tryb jasny',
        dark: 'Tryb ciemny',
      }
    }
  },
  en: {
    translation: {
      navbar: {
        client: 'Client Section',
        employee: 'Employee Section',
      },
      common: {
        back: 'Back',
        next: 'Next'
      },
      client: {
        checkVerification: {
          title: 'Check your verification',
          documentType: 'Document type',
          documentNumber: 'Document number',
          submit: 'Check',
          exists: 'Verification for the provided data exists.',
          notExists: 'No verification found for the provided data.',
          pesel: 'PESEL',
          passport: 'Passport',
          nip: 'NIP',
          docNumberRequired: 'Document number is required',
          invalidPesel: 'Invalid PESEL number',
          invalidNip: 'Invalid NIP number'
        },
        personalInfo: {
          title: 'Personal Data',
          firstName: 'First Name',
          lastName: 'Last Name',
          birthdate: 'Birthdate',
          nationality: 'Nationality',
          email: 'E-mail',
          phoneNumber: 'Phone Number',
          countryCode: 'Country Code',
          submit: 'Save Data',
          success: 'Data has been successfully saved.',
          loading: 'Loading verification...',
          error: 'An error occurred: {{message}}'
        },
        identityDocument: {
          title: 'Identity Document',
          type: 'Document Type',
          number: 'Document Number',
          image: 'Document Image',
          upload: 'Upload',
          selectFile: 'Select File',
          imageUploadedSuccess: 'Image uploaded successfully',
          imageLoadError: 'Error loading image'
        },
        stepper: {
          kyc: 'KYC Verification',
          bank: 'Bank Verification',
          ai: 'AI Recommendation',
          finalSummary: 'Final Summary'
        },
        bankVerification: {
          title: 'Bank Verification',
          description: 'Fetching your data from open banking requires your approval. Please make a decision.',
          approve: 'I approve the report',
          reject: 'I reject the report',
          success: 'Your decision has been saved.',
          alreadyApproved: 'Bank report has already been approved.',
          alreadyRejected: 'Bank report has been rejected.',
          waiting: 'Waiting for bank data...'
        },
        aiRecommendation: {
          title: 'AI Recommendation',
          description: 'Our AI has analyzed your data.',
          waiting: 'AI is analyzing data...'
        },
        finalSummary: {
          title: 'Final Summary',
          description: 'Here is the final decision regarding your verification.',
          waiting: 'Waiting for final decision...'
        }
      },
      employee: {
        createVerification: {
          title: 'Create new verification',
          documentType: 'Document type',
          documentNumber: 'Document number',
          submit: 'Start verification',
          success: 'Verification successfully created. ID: {{id}}',
          error: 'Error: {{message}}',
          pesel: 'PESEL',
          passport: 'Passport',
          nip: 'NIP',
          docNumberRequired: 'Document number is required',
          invalidPesel: 'Invalid PESEL number',
          invalidNip: 'Invalid NIP number'
        },
        verificationList: {
          title: 'Verification List',
          id: 'ID',
          status: 'Status',
          client: 'Client',
          document: 'Document',
          startDate: 'Start Date',
          finishDate: 'Finish Date',
          actions: 'Actions',
          statuses: {
            IN_PROGRESS: 'In Progress',
            VERIFIED: 'Verified',
            AWAITING_FINAL_DECISION: 'Awaiting Final Decision',
            FAILED: 'Failed'
          },
          noData: 'No verifications to display',
          rowsPerPage: 'Rows per page:',
          search: 'Search...',
          refresh: 'Refresh',
          filters: {
            title: 'Filters',
            clear: 'Clear filters',
            firstName: 'First Name',
            lastName: 'Last Name',
            documentNumber: 'Document Number',
            documentType: 'Document Type',
            nationality: 'Nationality',
            email: 'E-mail',
            phoneNumber: 'Phone Number',
            startDateFrom: 'Start Date From',
            startDateTo: 'Start Date To',
            status: 'Status'
          }
        },
        verificationDetails: {
          title: 'Verification Details',
          noClientData: 'Client has not provided personal data yet',
          noImage: 'Document image has not been uploaded yet',
          steps: {
            kyc: 'KYC Verification',
            bank: 'Bank Verification',
            ai: 'AI Recommendation',
            finalSummary: 'Final Summary',
            status: 'Status',
            statuses: {
              WAITING_FOR_PREVIOUS: 'Waiting',
              IN_PROGRESS: 'In Progress',
              FINISHED: 'Finished'
            }
          },
          bank: {
            title: 'Bank Verification',
            approval: 'Client Approval',
            approved: 'Approved',
            rejected: 'Rejected',
            notApproved: 'Bank verification consent not granted',
            noReport: 'No banking report available yet',
            reportPrepared: 'User not yet authorized open baking',
            reportInProgress: 'Report downloading has not finished yet',
            reportData: 'Report data:',
            owners: 'Owners',
            accounts: 'Accounts'
          },
          ai: {
            title: 'AI Recommendation',
            noRecommendation: 'No AI recommendation available yet',
            recommendationInProgress: 'AI recommendation generation has not finished yet',
            recommendationData: 'Recommendation content:'
          },
          final: {
            title: 'Final Decision',
            noDecision: 'No final decision available yet',
            decisionContent: 'Decision content:',
            error: 'An error occurred',
            decision: 'Decision',
            accepted: 'Accepted',
            rejected: 'Rejected',
            isAccepted: 'Verification result',
            agreementType: 'Agreement type',
            agreementNormal: 'Normal',
            agreementPrepaid: 'Pre-paid',
            placeholder: 'Enter decision justification...',
            submit: 'Send decision'
          }
        }
      },
      theme: {
        light: 'Light mode',
        dark: 'Dark mode',
      }
    }
  }
};

i18n
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    resources,
    fallbackLng: 'pl',
    interpolation: {
      escapeValue: false,
    }
  });

export default i18n;
