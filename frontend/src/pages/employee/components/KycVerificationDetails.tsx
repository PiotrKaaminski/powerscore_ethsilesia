import React, { FC } from 'react';
import {
  Paper,
  Typography,
  Box,
  Divider,
  CircularProgress
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import { VerificationInfo } from '../../../types/api';

interface KycVerificationDetailsProps {
  verification: VerificationInfo;
  isImageLoading: boolean;
  imageUrl: string | null;
}

const KycVerificationDetails: FC<KycVerificationDetailsProps> = ({ 
  verification, 
  isImageLoading, 
  imageUrl 
}) => {
  const { t } = useTranslation();

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
      {/* Sekcja: Dane Osobowe */}
      <Paper sx={{ p: 3 }}>
        <Typography variant="h6" gutterBottom color="primary">
          {t('client.personalInfo.title')}
        </Typography>
        <Divider sx={{ mb: 2 }} />
        {verification.client ? (
          <Box sx={{ display: 'flex', flexDirection: { xs: 'column', md: 'row' }, gap: { xs: 1, md: 4 } }}>
            <Box sx={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 1 }}>
              <Typography><strong>{t('client.personalInfo.firstName')}:</strong> {verification.client.firstName}</Typography>
              <Typography><strong>{t('client.personalInfo.lastName')}:</strong> {verification.client.lastName}</Typography>
              <Typography><strong>{t('client.personalInfo.birthdate')}:</strong> {verification.client.birthdate}</Typography>
            </Box>
            <Box sx={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 1 }}>
              <Typography><strong>{t('client.personalInfo.nationality')}:</strong> {verification.client.nationality}</Typography>
              <Typography><strong>{t('client.personalInfo.email')}:</strong> {verification.client.email}</Typography>
              <Typography><strong>{t('client.personalInfo.phoneNumber')}:</strong> {verification.client.phoneNumber}</Typography>
            </Box>
          </Box>
        ) : (
          <Typography color="text.secondary">{t('employee.verificationDetails.noClientData')}</Typography>
        )}
      </Paper>

      {/* Sekcja: Dokument Tożsamości */}
      <Paper sx={{ p: 3 }}>
        <Typography variant="h6" gutterBottom color="primary">
          {t('client.identityDocument.title')}
        </Typography>
        <Divider sx={{ mb: 2 }} />
        <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
          <Box sx={{ display: 'flex', flexDirection: { xs: 'column', md: 'row' }, gap: { xs: 1, md: 4 } }}>
            <Box sx={{ flex: 1 }}>
              <Typography><strong>{t('client.identityDocument.type')}:</strong> {verification.identityDocument.type}</Typography>
            </Box>
            <Box sx={{ flex: 1 }}>
              <Typography><strong>{t('client.identityDocument.number')}:</strong> {verification.identityDocument.number}</Typography>
            </Box>
          </Box>
          
          <Box sx={{ display: 'flex', justifyContent: 'center', minHeight: 200, alignItems: 'center', borderRadius: 1 }}>
            {isImageLoading ? (
              <CircularProgress />
            ) : imageUrl ? (
              <Box
                component="img"
                src={imageUrl}
                alt="Document"
                sx={{
                  maxWidth: '100%',
                  maxHeight: 400,
                  borderRadius: 1
                }}
              />
            ) : (
              <Typography color="text.secondary">
                {t('employee.verificationDetails.noImage')}
              </Typography>
            )}
          </Box>
        </Box>
      </Paper>
    </Box>
  );
};

export default KycVerificationDetails;
