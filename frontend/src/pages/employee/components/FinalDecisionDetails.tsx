import React, { FC, useState } from 'react';
import { Paper, Typography, Box, Divider, Stack, TextField, Button, FormControl, InputLabel, Select, MenuItem, Alert } from '@mui/material';
import { useTranslation } from 'react-i18next';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { VerificationInfo, AgreementType } from '../../../types/api';
import { sendFinalDecision } from '../../../api/verification';

interface FinalDecisionDetailsProps {
  verification: VerificationInfo;
}

const FinalDecisionDetails: FC<FinalDecisionDetailsProps> = ({ verification }) => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const decision = verification.finalDecision;

  const [agreementType, setAgreementType] = useState<AgreementType>('NORMAL');
  const [decisionData, setDecisionData] = useState('');
  const [isAccepted, setIsAccepted] = useState(true);

  const mutation = useMutation({
    mutationFn: () => sendFinalDecision(decision?.id || '', {
      agreementType,
      isAccepted,
      decisionData
    }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['verification', verification.id] });
    }
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    mutation.mutate();
  };

  return (
    <Paper elevation={3} sx={{ p: 3, mt: 3 }}>
      <Typography variant="h6" gutterBottom color="primary">
        {t('employee.verificationDetails.final.title')}
      </Typography>
      <Divider sx={{ mb: 2 }} />
      
      {!decision ? (
        <Typography color="text.secondary">
          {t('employee.verificationDetails.final.noDecision')}
        </Typography>
      ) : decision.status === 'FINISHED' ? (
        <Stack spacing={2}>
          <Box sx={{ display: 'flex', gap: 4, mb: 1 }}>
            <Box>
              <Typography variant="subtitle2" color="text.secondary">
                {t('employee.verificationDetails.final.isAccepted')}
              </Typography>
              <Typography variant="body1" sx={{ fontWeight: 'bold', color: decision.isAccepted ? 'success.main' : 'error.main' }}>
                {decision.isAccepted ? t('employee.verificationDetails.final.accepted') : t('employee.verificationDetails.final.rejected')}
              </Typography>
            </Box>
            {decision.isAccepted && (
              <Box>
                <Typography variant="subtitle2" color="text.secondary">
                  {t('employee.verificationDetails.final.agreementType')}
                </Typography>
                <Typography variant="body1">
                  {decision.agreementType === 'PRE_PAID' ? t('employee.verificationDetails.final.agreementPrepaid') : t('employee.verificationDetails.final.agreementNormal')}
                </Typography>
              </Box>
            )}
          </Box>

          {decision.decisionData && (
            <Box>
              <Typography variant="subtitle2" color="text.secondary" sx={{ mb: 1 }}>
                {t('employee.verificationDetails.final.decisionContent')}
              </Typography>
              <Paper 
                variant="outlined" 
                sx={{ 
                  p: 2, 
                  bgcolor: decision.isAccepted ? 'success.50' : 'error.50',
                  borderLeft: 6,
                  borderColor: decision.isAccepted ? 'success.main' : 'error.main'
                }}
              >
                <Typography variant="body1" sx={{ whiteSpace: 'pre-wrap' }}>
                  {decision.decisionData}
                </Typography>
              </Paper>
            </Box>
          )}
        </Stack>
      ) : (
        <Box component="form" onSubmit={handleSubmit}>
          <Stack spacing={3}>
            {mutation.isError && (
              <Alert severity="error">
                {mutation.error instanceof Error ? mutation.error.message : t('employee.verificationDetails.final.error')}
              </Alert>
            )}

            <FormControl fullWidth>
              <InputLabel id="is-accepted-label">{t('employee.verificationDetails.final.decision')}</InputLabel>
              <Select
                labelId="is-accepted-label"
                value={isAccepted ? 'YES' : 'NO'}
                label={t('employee.verificationDetails.final.decision')}
                onChange={(e) => setIsAccepted(e.target.value === 'YES')}
              >
                <MenuItem value="YES">{t('employee.verificationDetails.final.accepted')}</MenuItem>
                <MenuItem value="NO">{t('employee.verificationDetails.final.rejected')}</MenuItem>
              </Select>
            </FormControl>

            {isAccepted && (
              <FormControl fullWidth>
                <InputLabel id="agreement-type-label">{t('employee.verificationDetails.final.agreementType')}</InputLabel>
                <Select
                  labelId="agreement-type-label"
                  value={agreementType}
                  label={t('employee.verificationDetails.final.agreementType')}
                  onChange={(e) => setAgreementType(e.target.value as AgreementType)}
                >
                  <MenuItem value="NORMAL">{t('employee.verificationDetails.final.agreementNormal')}</MenuItem>
                  <MenuItem value="PRE_PAID">{t('employee.verificationDetails.final.agreementPrepaid')}</MenuItem>
                </Select>
              </FormControl>
            )}

            <TextField
              fullWidth
              multiline
              rows={4}
              label={t('employee.verificationDetails.final.decisionContent')}
              value={decisionData}
              onChange={(e) => setDecisionData(e.target.value)}
              placeholder={t('employee.verificationDetails.final.placeholder')}
            />

            <Button 
              type="submit" 
              variant="contained" 
              color="primary"
              disabled={mutation.isPending || !decisionData.trim()}
            >
              {t('employee.verificationDetails.final.submit')}
            </Button>
          </Stack>
        </Box>
      )}
    </Paper>
  );
};

export default FinalDecisionDetails;
