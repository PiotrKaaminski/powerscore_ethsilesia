import React, { FC, useState, useEffect, useRef } from 'react';
import { useParams, useLocation } from 'react-router-dom';
import { 
  Typography, 
  Container, 
  Box, 
  CircularProgress, 
  Alert,
  Button
} from '@mui/material';
import { 
  NavigateBefore as NavigateBeforeIcon,
  NavigateNext as NavigateNextIcon
} from '@mui/icons-material';
import { useQuery } from '@tanstack/react-query';
import { useTranslation } from 'react-i18next';
import { getVerificationById } from '../../../api/verification';
import VerificationStepper from './components/VerificationStepper';
import KycVerificationStep from './steps/KycVerificationStep';
import BankVerificationStep from './steps/BankVerificationStep';
import AiSummaryStep from './steps/AiSummaryStep';
import FinalSummaryStep from './steps/FinalSummaryStep';

const VerificationProcess: FC = () => {
  const { id } = useParams<{ id: string }>();
  const location = useLocation();
  const { t } = useTranslation();
  const [activeStep, setActiveStep] = useState<number>(0);
  const initialStepSet = useRef(false);

  const { data: verification, isLoading, error } = useQuery({
    queryKey: ['verification', id],
    queryFn: () => getVerificationById(id!),
    enabled: !!id,
    refetchInterval: (query) => {
      const data = query.state.data;
      if (data) {
        // Refresh if bank verification is in progress and approved (waiting for report)
        if (data.bankVerificationStatus === 'IN_PROGRESS' && data.openBankingReport?.status === 'IN_PROGRESS') {
          return 3000;
        }
        // Refresh if AI recommendation is in progress
        if (data.aiRecommendationStatus === 'IN_PROGRESS') {
          return 3000;
        }
        // Refresh if Final summary is in progress
        if (data.finalSummaryStatus === 'IN_PROGRESS') {
          return 3000;
        }
      }
      return false;
    }
  });

  useEffect(() => {
    if (verification && !initialStepSet.current) {
      const fromFindVerification = location.state?.fromFindVerification === true;
      if (fromFindVerification) {
        if (verification.finalSummaryStatus === 'IN_PROGRESS') {
          setActiveStep(3);
        } else if (verification.aiRecommendationStatus === 'IN_PROGRESS') {
          setActiveStep(2);
        } else if (verification.bankVerificationStatus === 'IN_PROGRESS') {
          setActiveStep(1);
        } else if (verification.kycVerificationStatus === 'IN_PROGRESS') {
          setActiveStep(0);
        } else if (verification.finalSummaryStatus === 'FINISHED') {
          setActiveStep(3);
        }
      }
      initialStepSet.current = true;
    }
  }, [verification, location.state]);

  if (isLoading) {
    return (
      <Container maxWidth="sm">
        <Box sx={{ mt: 8, display: 'flex', justifyContent: 'center' }}>
          <CircularProgress />
          <Typography sx={{ ml: 2 }}>{t('client.personalInfo.loading')}</Typography>
        </Box>
      </Container>
    );
  }

  if (error || !verification) {
    return (
      <Container maxWidth="sm">
        <Box sx={{ mt: 4 }}>
          <Alert severity="error">
            {t('client.personalInfo.error', { message: (error as Error)?.message || 'Verification not found' })}
          </Alert>
        </Box>
      </Container>
    );
  }

  const renderStep = () => {
    switch (activeStep) {
      case 0:
        return (
          <KycVerificationStep 
            verificationId={id!}
            clientData={verification.client}
            documentData={verification.identityDocument}
          />
        );
      case 1:
        return <BankVerificationStep verification={verification} />;
      case 2:
        return <AiSummaryStep verification={verification} />;
      case 3:
        return <FinalSummaryStep verification={verification} />;
      default:
        return null;
    }
  };

  const handleStepClick = (stepIndex: number) => {
    if (!verification) return;

    let canNavigate = false;
    switch (stepIndex) {
      case 0:
        canNavigate = verification.kycVerificationStatus !== 'WAITING_FOR_PREVIOUS';
        break;
      case 1:
        canNavigate = verification.bankVerificationStatus !== 'WAITING_FOR_PREVIOUS';
        break;
      case 2:
        canNavigate = verification.aiRecommendationStatus !== 'WAITING_FOR_PREVIOUS';
        break;
      case 3:
        canNavigate = verification.finalSummaryStatus !== 'WAITING_FOR_PREVIOUS';
        break;
    }

    if (canNavigate) {
      setActiveStep(stepIndex);
    }
  };

  const isNextDisabled = () => {
    if (!verification) return true;
    const nextStep = activeStep + 1;
    if (nextStep > 3) return true;

    switch (nextStep) {
      case 1:
        return verification.bankVerificationStatus === 'WAITING_FOR_PREVIOUS';
      case 2:
        return verification.aiRecommendationStatus === 'WAITING_FOR_PREVIOUS';
      case 3:
        return verification.finalSummaryStatus === 'WAITING_FOR_PREVIOUS';
      default:
        return true;
    }
  };

  return (
    <Container maxWidth="md">
      <Box sx={{ mt: 4, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <VerificationStepper 
          kycStatus={verification.kycVerificationStatus}
          bankStatus={verification.bankVerificationStatus}
          aiStatus={verification.aiRecommendationStatus}
          finalSummaryStatus={verification.finalSummaryStatus}
          activeStep={activeStep}
          onStepClick={handleStepClick}
        />
        <Box sx={{ width: '100%', mt: 4 }}>
          {renderStep()}
        </Box>

        <Box sx={{ display: 'flex', justifyContent: 'space-between', width: '100%', mt: 4, mb: 4 }}>
          <Button
            startIcon={<NavigateBeforeIcon />}
            onClick={() => setActiveStep((prev) => prev - 1)}
            disabled={activeStep === 0}
          >
            {t('common.back')}
          </Button>
          <Button
            endIcon={<NavigateNextIcon />}
            onClick={() => setActiveStep((prev) => prev + 1)}
            disabled={activeStep === 3 || isNextDisabled()}
          >
            {t('common.next')}
          </Button>
        </Box>
      </Box>
    </Container>
  );
};

export default VerificationProcess;
