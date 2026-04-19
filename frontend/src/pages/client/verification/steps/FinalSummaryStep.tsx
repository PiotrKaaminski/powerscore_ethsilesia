import React, { FC } from 'react';
import { Typography, Paper, Box, CircularProgress, Alert, Stack } from '@mui/material';
import { useTranslation } from 'react-i18next';
import { VerificationInfo } from '../../../../types/api';

interface FinalSummaryStepProps {
  verification: VerificationInfo;
}

const FinalSummaryStep: FC<FinalSummaryStepProps> = ({ verification }) => {
  const { t } = useTranslation();

  const isFinished = verification.finalSummaryStatus === 'FINISHED';
  const finalDecision = verification.finalDecision;

  return (
    <Paper elevation={3} sx={{ p: 4, width: '100%', mt: 2 }}>
      <Typography variant="h5" gutterBottom>
        {t('client.finalSummary.title')}
      </Typography>

      {!isFinished && (
        <Stack spacing={2} sx={{ alignItems: 'center' }}>
          <CircularProgress size={24} />
          <Typography variant="body1">
            {t('client.finalSummary.waiting')}
          </Typography>
        </Stack>
      )}

      {isFinished && finalDecision && (
        <Box>
          <Typography variant="body1" sx={{ mb: 2 }}>
            {t('client.finalSummary.description')}
          </Typography>
          <Paper
            variant="outlined"
            sx={{
              p: 3,
              bgcolor: finalDecision.isAccepted ? 'success.50' : 'error.50',
              borderLeft: 6,
              borderColor: finalDecision.isAccepted ? 'success.main' : 'error.main'
            }}
          >
            <Typography variant="h6" gutterBottom color={finalDecision.isAccepted ? 'success.dark' : 'error.dark'}>
              {finalDecision.isAccepted ? t('employee.verificationDetails.final.accepted') : t('employee.verificationDetails.final.rejected')}
            </Typography>

            {finalDecision.isAccepted && finalDecision.agreementType && (
              <Typography variant="subtitle2" sx={{ mb: 1 }}>
                {t('employee.verificationDetails.final.agreementType')}: {finalDecision.agreementType === 'PRE_PAID' ? t('employee.verificationDetails.final.agreementPrepaid') : t('employee.verificationDetails.final.agreementNormal')}
              </Typography>
            )}

            <Typography variant="body1" sx={{ whiteSpace: 'pre-wrap' }}>
              {finalDecision.decisionData}
            </Typography>
          </Paper>
        </Box>
      )}

      {isFinished && !finalDecision && (
        <Alert severity="warning">
          {t('employee.verificationDetails.final.noDecision')}
        </Alert>
      )}
    </Paper>
  );
};

export default FinalSummaryStep;
