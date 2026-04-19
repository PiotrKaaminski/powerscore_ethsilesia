export type DocumentType = 'PESEL' | 'PASSPORT' | 'NIP';

export interface CreateVerificationRequest {
  type: DocumentType;
  number: string;
}

export type CreateVerificationResultType = 'EXISTING' | 'NEW';

export interface CreateVerificationSuccess {
  verificationId: string;
  type: CreateVerificationResultType;
}

export interface PersonalDataRequest {
  firstName: string;
  lastName: string;
  birthdate: string;
  nationality: string;
  email: string;
  phoneNumber: string;
}

export interface AddPersonalDataSuccess {
  clientId: string;
}

export interface IdentityDocumentInfo {
  id: string;
  type: DocumentType;
  number: string;
  imageUploaded: boolean;
}

export interface ClientInfo {
  id: string;
  firstName: string;
  lastName: string;
  birthdate: string;
  nationality: string;
  email: string;
  phoneNumber: string;
}

export type VerificationStatus = 'IN_PROGRESS' | 'VERIFIED' | 'AWAITING_FINAL_DECISION' | 'FAILED';

export type VerificationStepStatus = 'WAITING_FOR_PREVIOUS' | 'IN_PROGRESS' | 'FINISHED';

export type OpenBankingStatus = 'PREPARED' | 'IN_PROGRESS' | 'FINISHED';

export interface OpenBankingReportInfo {
  id: string;
  status: OpenBankingStatus;
  reportData: any;
}

export type AiRecommendationStatus = 'IN_PROGRESS' | 'FINISHED' | 'FAILED';

export interface AiRecommendationInfo {
  id: string;
  status: AiRecommendationStatus;
  recommendationData: any;
}

export type FinalDecisionStatus = 'IN_PROGRESS' | 'FINISHED';

export interface FinalDecisionInfo {
  id: string;
  status: FinalDecisionStatus;
  decisionData: string;
  agreementType?: AgreementType;
  isAccepted?: boolean;
}

export interface VerificationInfo {
  id: string;
  status: VerificationStatus;
  client?: ClientInfo;
  identityDocument: IdentityDocumentInfo;
  startDate: string;
  finishDate?: string;
  kycVerificationStatus: VerificationStepStatus;
  bankVerificationStatus: VerificationStepStatus;
  aiRecommendationStatus: VerificationStepStatus;
  finalSummaryStatus: VerificationStepStatus;
  bankVerificationApproved?: boolean;
  openBankingReport?: OpenBankingReportInfo;
  aiRecommendation?: AiRecommendationInfo;
  finalDecision?: FinalDecisionInfo;
}

export interface ApiError {
  message: string;
  errorCode: string;
}

export type VerificationSortBy = 
  | 'VERIFICATION_STATUS' 
  | 'FIRST_NAME' 
  | 'LAST_NAME' 
  | 'BIRTHDATE' 
  | 'NATIONALITY' 
  | 'EMAIL' 
  | 'PHONE_NUMBER' 
  | 'IDENTITY_DOCUMENT_TYPE' 
  | 'IDENTITY_DOCUMENT_NUMBER' 
  | 'START_DATE' 
  | 'FINISH_DATE';

export type SortOrder = 'ASC' | 'DESC';

export interface VerificationFilterParams {
  sortBy?: VerificationSortBy;
  sortOrder?: SortOrder;
  'paginationFilter.page'?: number;
  'paginationFilter.pageSize'?: number;
  statuses?: VerificationStatus[];
  'startDate.from'?: string;
  'startDate.to'?: string;
  'endDate.from'?: string;
  'endDate.to'?: string;
  'clientFilters.firstName'?: string;
  'clientFilters.lastName'?: string;
  'clientFilters.birthdate.from'?: string;
  'clientFilters.birthdate.to'?: string;
  'clientFilters.nationality'?: string;
  'clientFilters.email'?: string;
  'clientFilters.phoneNumber'?: string;
  'identityDocumentFilters.identityDocumentTypes'?: DocumentType[];
  'identityDocumentFilters.number'?: string;
}

export interface PaginatedResponse<T> {
  rows: T[];
  totalElements: number;
  totalPages: number;
  page: number;
  size: number;
}

export interface HandleBankingReportApprovalRequest {
  clientApproval: boolean;
}

export type AgreementType = 'PRE_PAID' | 'NORMAL';

export interface SendFinalDecisionRequest {
  agreementType: AgreementType;
  isAccepted: boolean;
  decisionData: string;
}

export interface StartBankingReportRequest {
  sessionId: string;
  sessionIdSignature: string;
  ownerExternalId: string;
}

export type PaginatedVerificationInfo = PaginatedResponse<VerificationInfo>;
