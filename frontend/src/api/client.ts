import { ClientInfo } from '../types/api';

const BASE_URL = process.env.REACT_APP_API_URL || '';

export const getClientById = async (
  id: string
): Promise<ClientInfo> => {
  const response = await fetch(`${BASE_URL}/api/client/${id}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  });

  if (!response.ok) {
    let errorMessage = 'Wystąpił błąd podczas pobierania danych klienta';
    try {
      const errorData = await response.json();
      errorMessage = errorData.message || errorMessage;
    } catch (e) {
      // Ignorujemy błąd parsowania
    }
    throw new Error(errorMessage);
  }

  return response.json();
};
