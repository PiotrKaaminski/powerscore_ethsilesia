import React, { FC } from 'react';
import { Box, Paper, Typography, CircularProgress, Alert, Stack } from '@mui/material';
import { useTranslation } from 'react-i18next';
import { VerificationInfo } from '../../../../types/api';

interface AiSummaryStepProps {
  verification: VerificationInfo;
}

const AiSummaryStep: FC<AiSummaryStepProps> = ({ verification }) => {
  const { t } = useTranslation();

  const isFinished = verification.aiRecommendationStatus === 'FINISHED';
  const recommendation = verification.aiRecommendation;

  return (
    <Box sx={{ width: '100%' }}>
      <Paper elevation={3} sx={{ p: 4, width: '100%', mt: 2 }}>
        <Typography variant="h5" gutterBottom>
          {t('client.aiRecommendation.title')}
        </Typography>

        {!isFinished && (
          <Stack spacing={2} sx={{ alignItems: 'center' }}>
            <CircularProgress size={24} />
            <Typography variant="body1">
              {t('client.aiRecommendation.waiting')}
            </Typography>
          </Stack>
        )}

        {isFinished && recommendation && (
          <Box>
            <Typography variant="body1" sx={{ mb: 2 }}>
              {t('client.aiRecommendation.description')}
            </Typography>
            {(() => {
              let data: any;
              try {
                data = typeof recommendation.recommendationData === 'string' 
                  ? JSON.parse(recommendation.recommendationData) 
                  : recommendation.recommendationData;
              } catch (e) {
                return <Alert severity="error">Błąd podczas przetwarzania danych rekomendacji.</Alert>;
              }

              if (!data) return null;

              return (
                <Stack spacing={2}>
                  <Box sx={{ display: 'flex', gap: 2 }}>
                    <Paper variant="outlined" sx={{ p: 2, flex: 1, bgcolor: 'primary.50' }}>
                      <Typography variant="subtitle2" color="text.secondary">
                        Scoring
                      </Typography>
                      <Typography variant="h6">
                        {data.scoring}
                      </Typography>
                    </Paper>
                    <Paper variant="outlined" sx={{ p: 2, flex: 1, bgcolor: 'primary.50' }}>
                      <Typography variant="subtitle2" color="text.secondary">
                        {t('employee.verificationDetails.final.agreementType')}
                      </Typography>
                      <Typography variant="h6">
                        {data.agreementType}
                      </Typography>
                    </Paper>
                  </Box>
                  <Paper variant="outlined" sx={{ p: 2, bgcolor: 'primary.50' }}>
                    <Typography variant="subtitle2" color="text.secondary" sx={{ mb: 1 }}>
                      {t('employee.verificationDetails.final.decisionContent')}
                    </Typography>
                    <Typography variant="body2" sx={{ whiteSpace: 'pre-wrap' }}>
                      {data.explanation}
                    </Typography>
                  </Paper>
                </Stack>
              );
            })()}
          </Box>
        )}

        {isFinished && !recommendation && (
          <Alert severity="warning">
            Brak danych rekomendacji AI.
          </Alert>
        )}
      </Paper>
    </Box>
  );
};

export default AiSummaryStep;
