/**
 * Walidacja numeru PESEL zgodnie z algorytmem sumy kontrolnej.
 */
export const isValidPesel = (pesel: string): boolean => {
  // PESEL musi mieć dokładnie 11 cyfr
  const peselRegex = /^[0-9]{11}$/;
  if (!peselRegex.test(pesel)) {
    return false;
  }

  const digits = pesel.split('').map(Number);
  const weights = [1, 3, 7, 9, 1, 3, 7, 9, 1, 3];
  
  let sum = 0;
  for (let i = 0; i < 10; i++) {
    sum += digits[i] * weights[i];
  }

  const checksum = (10 - (sum % 10)) % 10;

  return digits[10] === checksum;
};

/**
 * Walidacja numeru NIP (Numer Identyfikacji Podatkowej).
 */
export const isValidNip = (nip: string): boolean => {
  // NIP może być podany z myślnikami, usuwamy je do walidacji
  const cleanNip = nip.replace(/-/g, '');
  
  // NIP musi mieć dokładnie 10 cyfr
  const nipRegex = /^[0-9]{10}$/;
  if (!nipRegex.test(cleanNip)) {
    return false;
  }

  const digits = cleanNip.split('').map(Number);
  const weights = [6, 5, 7, 2, 3, 4, 5, 6, 7];
  
  let sum = 0;
  for (let i = 0; i < 9; i++) {
    sum += digits[i] * weights[i];
  }

  const checksum = sum % 11;
  
  // Suma kontrolna modulo 11 nie może być równa 10 (taki NIP jest nieprawidłowy)
  if (checksum === 10) {
    return false;
  }

  return digits[9] === checksum;
};
