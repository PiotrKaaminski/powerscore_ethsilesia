import React, { FC } from 'react';
import { Paper, Typography, Box, Chip, Divider, Stack, Alert } from '@mui/material';
import { useTranslation } from 'react-i18next';
import { VerificationInfo } from '../../../types/api';
import ReportDataView from './ReportDataView';

interface BankVerificationDetailsProps {
  verification: VerificationInfo;
}

const BankVerificationDetails: FC<BankVerificationDetailsProps> = ({ verification }) => {
  const { t } = useTranslation();
  const report = verification.openBankingReport;

  return (
    <Paper elevation={3} sx={{ p: 3, mt: 3 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
        <Typography variant="h6" gutterBottom color="primary">
          {t('employee.verificationDetails.bank.title')}
        </Typography>
        {verification.bankVerificationApproved !== null && verification.bankVerificationApproved !== undefined && (
          <Chip 
            label={verification.bankVerificationApproved 
              ? t('employee.verificationDetails.bank.approved') 
              : t('employee.verificationDetails.bank.rejected')}
            color={verification.bankVerificationApproved ? 'success' : 'error'}
          />
        )}
      </Box>
      <Divider sx={{ mb: 2 }} />
      
      {verification.bankVerificationApproved === false ? (
        <Alert severity="error" sx={{ mb: 2 }}>
          {t('employee.verificationDetails.bank.notApproved')}
        </Alert>
      ) : !report ? (
        <Alert severity="info" sx={{ mb: 2 }}>
          {t('employee.verificationDetails.bank.noReport')}
        </Alert>
      ) : report.status === 'PREPARED' ? (
        <Alert severity="info" sx={{ mb: 2 }}>
          {t('employee.verificationDetails.bank.reportPrepared')}
        </Alert>
      ) : report.status === 'IN_PROGRESS' ? (
        <Alert severity="info" sx={{ mb: 2 }}>
          {t('employee.verificationDetails.bank.reportInProgress')}
        </Alert>
      ) : (
        <Stack spacing={2}>
          {report.reportData && (
            <Box>
              <Typography variant="subtitle2" color="text.secondary" sx={{ mb: 1 }}>
                {t('employee.verificationDetails.bank.reportData')}
              </Typography>
              <ReportDataView data={report.reportData} />
            </Box>
          )}
        </Stack>
      )}
    </Paper>
  );
};

export default BankVerificationDetails;
