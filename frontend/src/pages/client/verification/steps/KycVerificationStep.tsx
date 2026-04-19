import React, { FC } from 'react';
import { Paper, Box } from '@mui/material';
import PersonalDataForm from '../components/PersonalDataForm';
import IdentityDocumentForm from '../components/IdentityDocumentForm';
import { ClientInfo, IdentityDocumentInfo } from '../../../../types/api';

interface KycVerificationStepProps {
  verificationId: string;
  clientData?: ClientInfo;
  documentData: IdentityDocumentInfo;
}

const KycVerificationStep: FC<KycVerificationStepProps> = ({ 
  verificationId, 
  clientData, 
  documentData 
}) => {
  return (
    <Box sx={{ width: '100%' }}>
      <Paper elevation={3} sx={{ p: 4, width: '100%', mt: 2 }}>
        <PersonalDataForm 
          verificationId={verificationId}
          clientData={clientData}
        />
      </Paper>

      <Paper elevation={3} sx={{ p: 4, width: '100%', mt: 4 }}>
        <IdentityDocumentForm 
          verificationId={verificationId}
          documentData={documentData}
        />
      </Paper>
    </Box>
  );
};

export default KycVerificationStep;
