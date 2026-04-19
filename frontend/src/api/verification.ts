import { 
  CreateVerificationRequest, 
  CreateVerificationSuccess, 
  VerificationInfo, 
  DocumentType, 
  PersonalDataRequest, 
  AddPersonalDataSuccess,
  VerificationFilterParams,
  PaginatedVerificationInfo,
  HandleBankingReportApprovalRequest,
  SendFinalDecisionRequest,
  StartBankingReportRequest
} from '../types/api';

const BASE_URL = process.env.REACT_APP_API_URL || '';

export const getAllVerifications = async (
  params: VerificationFilterParams = {}
): Promise<PaginatedVerificationInfo> => {
  const queryParams = new URLSearchParams();
  
  const formatValue = (v: any): string => {
    const str = v.toString();
    if (typeof v === 'string' && str.endsWith('Z') && str.includes('T')) {
      return str.replace('Z', '');
    }
    return str;
  };
  
  Object.entries(params).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== '') {
      if (Array.isArray(value)) {
        value.forEach(v => queryParams.append(key, formatValue(v)));
      } else {
        queryParams.append(key, formatValue(value));
      }
    }
  });

  const url = `${BASE_URL}/api/verifications${queryParams.toString() ? `?${queryParams.toString()}` : ''}`;
  console.log('[DEBUG_LOG] Fetching verifications from:', url);
  
  const response = await fetch(url, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  });

  if (!response.ok) {
    let errorMessage = 'Wystąpił błąd podczas pobierania listy weryfikacji';
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

export const createVerification = async (
  request: CreateVerificationRequest
): Promise<CreateVerificationSuccess> => {
  const response = await fetch(`${BASE_URL}/api/verification`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(request),
  });

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.message || 'Wystąpił błąd podczas tworzenia weryfikacji');
  }

  return response.json();
};

export const addPersonalData = async (
  id: string,
  request: PersonalDataRequest
): Promise<AddPersonalDataSuccess> => {
  const response = await fetch(`${BASE_URL}/api/verifications/${id}/personalData`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(request),
  });

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.message || 'Wystąpił błąd podczas dodawania danych osobowych');
  }

  return response.json();
};

export const getVerificationById = async (
  id: string
): Promise<VerificationInfo> => {
  const response = await fetch(`${BASE_URL}/api/verifications/${id}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  });

  if (!response.ok) {
    let errorMessage = 'Wystąpił błąd podczas pobierania weryfikacji';
    try {
      const errorData = await response.json();
      errorMessage = errorData.message || errorMessage;
    } catch (e) {
      // Jeśli nie można sparsować JSONa, używamy domyślnego błędu
    }
    throw new Error(errorMessage);
  }

  return response.json();
};

export const getVerificationByDocument = async (
  type: DocumentType,
  number: string
): Promise<VerificationInfo | null> => {
  const params = new URLSearchParams({ type, number });
  const response = await fetch(`${BASE_URL}/api/verifications/byDocument?${params.toString()}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  });

  if (response.status === 404) {
    return null;
  }

  if (!response.ok) {
    let errorMessage = 'Wystąpił błąd podczas sprawdzania weryfikacji';
    try {
      const errorData = await response.json();
      errorMessage = errorData.message || errorMessage;
    } catch (e) {
      // Jeśli nie można sparsować JSONa, używamy domyślnego błędu
    }
    throw new Error(errorMessage);
  }

  return response.json();
};

export const uploadDocumentImage = async (
  id: string,
  file: File
): Promise<void> => {
  const formData = new FormData();
  formData.append('file', file);

  const response = await fetch(`${BASE_URL}/api/verifications/${id}/documentImage`, {
    method: 'POST',
    body: formData,
  });

  if (!response.ok) {
    let errorMessage = 'Wystąpił błąd podczas wgrywania zdjęcia';
    try {
      const errorData = await response.json();
      errorMessage = errorData.message || errorMessage;
    } catch (e) {
      // Ignorujemy błąd parsowania
    }
    throw new Error(errorMessage);
  }
};

export const getDocumentImage = async (
  id: string
): Promise<Blob> => {
  const response = await fetch(`${BASE_URL}/api/verifications/${id}/documentImage`, {
    method: 'GET',
  });

  if (!response.ok) {
    throw new Error('Nie udało się pobrać zdjęcia dokumentu');
  }

  return response.blob();
};

export const provideBankingReportApproval = async (
  id: string,
  request: HandleBankingReportApprovalRequest
): Promise<void> => {
  const response = await fetch(`${BASE_URL}/api/verifications/${id}/bankingReportApproval`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(request),
  });

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.message || 'Wystąpił błąd podczas zatwierdzania raportu bankowego');
  }
};

export const startBankingReport = async (
  id: string,
  request: StartBankingReportRequest
): Promise<void> => {
  const response = await fetch(`${BASE_URL}/api/openBanking/${id}/startReport`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(request),
  });

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.message || 'Wystąpił błąd podczas rozpoczynania raportu bankowego');
  }
};

export const sendFinalDecision = async (
  id: string,
  request: SendFinalDecisionRequest
): Promise<void> => {
  const response = await fetch(`${BASE_URL}/api/finalDecision/${id}/sendDecision`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(request),
  });

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.message || 'Wystąpił błąd podczas wysyłania decyzji');
  }
};
