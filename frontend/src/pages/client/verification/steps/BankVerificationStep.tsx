import React, { FC, useEffect } from 'react';
import { Box, Paper, Typography, Button, Alert, Stack, CircularProgress } from '@mui/material';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { useTranslation } from 'react-i18next';
import { provideBankingReportApproval, startBankingReport } from '../../../../api/verification';
import { VerificationInfo } from '../../../../types/api';
import ReportDataView from '../../../employee/components/ReportDataView';

declare global {
  interface Window {
    embedKontomatik: any;
  }
}

interface BankVerificationStepProps {
  verification: VerificationInfo;
}

const BankVerificationStep: FC<BankVerificationStepProps> = ({ verification }) => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const verificationId = verification.id;

  const mutation = useMutation({
    mutationFn: (approval: boolean) => provideBankingReportApproval(verificationId, { clientApproval: approval }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['verification', verificationId] });
    },
  });
  const openBankingReport = verification.openBankingReport;
  const isFinished = verification.bankVerificationStatus === 'FINISHED';
  const needsApproval = verification.bankVerificationStatus === 'IN_PROGRESS' &&
                                verification.bankVerificationApproved === null;
  const needsOpenBankingAuth = verification.bankVerificationStatus === 'IN_PROGRESS' &&
                                openBankingReport?.status === 'PREPARED';
  const isDownloadingReport = verification.bankVerificationStatus === 'IN_PROGRESS' &&
                                  openBankingReport?.status === 'IN_PROGRESS';

  const isMutating = React.useRef(false);

  const startReportMutation = useMutation({
    mutationFn: (request: { id: string; sessionId: string; sessionIdSignature: string; ownerExternalId: string }) =>
      startBankingReport(request.id, {
        sessionId: request.sessionId,
        sessionIdSignature: request.sessionIdSignature,
        ownerExternalId: request.ownerExternalId,
      }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['verification', verificationId] });
    },
    onSettled: () => {
      isMutating.current = false;
    }
  });

  useEffect(() => {
    if (needsOpenBankingAuth && openBankingReport) {
      const scriptId = 'kontomatik-script';
      let script = document.getElementById(scriptId) as HTMLScriptElement;

      const initKontomatik = () => {
        if (window.embedKontomatik && document.getElementById('kontomatik-widget')) {
          const randomExternalId = "TEST_OwnerID_" + (Date.now() + Math.floor((Math.random() * 1000) + 1));
          window.embedKontomatik({
            client: 'jziomek-test', // replace it with your assigned client id
            divId: 'kontomatik-widget',
            ownerExternalId: randomExternalId,
            onSuccess: (target: any, sessionId: string, sessionIdSignature: string, options: any) => {
              if (isMutating.current) return;
              isMutating.current = true;
              
              startReportMutation.mutate({
                id: openBankingReport.id,
                sessionId,
                sessionIdSignature,
                ownerExternalId: randomExternalId,
              });
            },
          });
        }
      };

      if (!script) {
        script = document.createElement('script');
        script.id = scriptId;
        script.src = 'https://signin.kontomatik.com/assets/signin-widget.js';
        script.async = true;
        script.onload = () => {
          // Małe opóźnienie, aby upewnić się, że DOM jest gotowy
          setTimeout(initKontomatik, 100);
        };
        document.head.appendChild(script);
      } else {
        // Jeśli skrypt już jest, inicjalizujemy po krótkim opóźnieniu dla renderowania kontenera
        setTimeout(initKontomatik, 100);
      }
    }
  }, [needsOpenBankingAuth, openBankingReport, startReportMutation, verificationId, queryClient]);

  return (
    <Box sx={{ width: '100%' }}>
      <Paper elevation={3} sx={{ p: 4, width: '100%', mt: 2 }}>
        <Typography variant="h5" gutterBottom>
          {t('client.bankVerification.title')}
        </Typography>

        {isDownloadingReport && (
          <Stack spacing={2} sx={{ alignItems: 'center' }}>
            <CircularProgress size={24} />
            <Typography variant="body1">
              {t('client.bankVerification.waiting')}
            </Typography>
          </Stack>
        )}

        {needsApproval && (
          <Box>
            <Typography variant="body1" sx={{ mb: 2 }}>
              {t('client.bankVerification.description')}
            </Typography>
            {verification.openBankingReport?.reportData && (
              <Box>
                <Typography variant="subtitle2" color="text.secondary" sx={{ mb: 1 }}>
                  {t('employee.verificationDetails.bank.reportData')}
                </Typography>
                <ReportDataView data={verification.openBankingReport.reportData} />
              </Box>
            )}
            <Stack direction="row" spacing={2} sx={{ justifyContent: 'center' }}>
              <Button
                variant="contained"
                color="primary"
                onClick={() => mutation.mutate(true)}
                disabled={mutation.isPending}
              >
                {t('client.bankVerification.approve')}
              </Button>
              <Button
                variant="outlined"
                color="error"
                onClick={() => mutation.mutate(false)}
                disabled={mutation.isPending}
              >
                {t('client.bankVerification.reject')}
              </Button>
            </Stack>
          </Box>
        )}

        {needsOpenBankingAuth && (
          <Box sx={{ mt: 2, minHeight: '400px', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            {startReportMutation.isPending && (
              <Stack spacing={2} sx={{ alignItems: 'center', mb: 2 }}>
                <CircularProgress size={24} />
                <Typography variant="body2">
                  Trwa rozpoczynanie raportu...
                </Typography>
              </Stack>
            )}
            <div id="kontomatik-widget" style={{ width: '100%', display: 'flex', flexDirection: 'column', alignItems: 'center' }} />
          </Box>
        )}

        {isFinished && (
          <Box>
            {!verification.bankVerificationApproved && (
              <Alert severity="warning" sx={{ mb: 2 }}>
                {t('client.bankVerification.alreadyRejected')}
              </Alert>
            )}

            {verification.bankVerificationApproved && verification.openBankingReport?.reportData && (
              <Box>
                <Typography variant="subtitle2" color="text.secondary" sx={{ mb: 1 }}>
                  {t('employee.verificationDetails.bank.reportData')}
                </Typography>
                <ReportDataView data={verification.openBankingReport.reportData} />
              </Box>
            )}
          </Box>
        )}
      </Paper>
    </Box>
  );
};

export default BankVerificationStep;
