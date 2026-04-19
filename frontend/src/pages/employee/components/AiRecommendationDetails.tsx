import React, { FC } from 'react';
import { Paper, Typography, Box, Divider, Stack, Alert } from '@mui/material';
import { useTranslation } from 'react-i18next';
import { VerificationInfo } from '../../../types/api';

interface AiRecommendationDetailsProps {
  verification: VerificationInfo;
}

const AiRecommendationDetails: FC<AiRecommendationDetailsProps> = ({ verification }) => {
  const { t } = useTranslation();
  const recommendation = verification.aiRecommendation;

  return (
    <Paper elevation={3} sx={{ p: 3, mt: 3 }}>
      <Typography variant="h6" gutterBottom color="primary">
        {t('employee.verificationDetails.ai.title')}
      </Typography>
      <Divider sx={{ mb: 2 }} />
      
      {!recommendation ? (
        <Alert severity="info" sx={{ mb: 2 }}>
          {t('employee.verificationDetails.ai.noRecommendation')}
        </Alert>
      ) : recommendation.status === 'IN_PROGRESS' ? (
        <Alert severity="info" sx={{ mb: 2 }}>
          {t('employee.verificationDetails.ai.recommendationInProgress')}
        </Alert>
      ) : (
        <Stack spacing={2}>
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
        </Stack>
      )}
    </Paper>
  );
};

export default AiRecommendationDetails;
