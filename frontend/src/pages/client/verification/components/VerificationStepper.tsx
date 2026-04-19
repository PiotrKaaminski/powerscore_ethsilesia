import React, { FC } from 'react';
import { 
  Box, 
  Typography, 
  Tooltip,
  useTheme
} from '@mui/material';
import { CheckCircle, FiberManualRecord, RadioButtonUnchecked } from '@mui/icons-material';
import { VerificationStepStatus } from '../../../../types/api';
import { useTranslation } from 'react-i18next';

interface StepProps {
  status: VerificationStepStatus;
  label: string;
  onClick: () => void;
  isActive: boolean;
}

const StepCircle: FC<StepProps> = ({ status, label, onClick, isActive }) => {
  const theme = useTheme();
  const isClickable = status === 'FINISHED' || status === 'IN_PROGRESS';
  
  const getColor = () => {
    switch (status) {
      case 'FINISHED':
        return isActive ? theme.palette.success.dark : theme.palette.success.main;
      case 'IN_PROGRESS':
        return theme.palette.primary.main;
      case 'WAITING_FOR_PREVIOUS':
      default:
        return theme.palette.grey[500];
    }
  };

  const getIcon = () => {
    if (isActive && status !== 'IN_PROGRESS') {
       return <FiberManualRecord sx={{ color: getColor(), border: status === 'FINISHED' ? `2px solid ${theme.palette.success.main}` : 'none', borderRadius: '50%' }} />;
    }
    switch (status) {
      case 'FINISHED':
        return <CheckCircle sx={{ color: getColor() }} />;
      case 'IN_PROGRESS':
        return <FiberManualRecord sx={{ color: getColor() }} />;
      case 'WAITING_FOR_PREVIOUS':
      default:
        return <RadioButtonUnchecked sx={{ color: getColor() }} />;
    }
  };

  return (
    <Box 
      onClick={isClickable ? onClick : undefined}
      sx={{ 
        display: 'flex', 
        flexDirection: 'column', 
        alignItems: 'center', 
        flex: '1 1 0',
        minWidth: 80,
        cursor: isClickable ? 'pointer' : 'default',
        '&:hover': isClickable ? {
          opacity: 0.8
        } : {}
      }}
    >
      <Tooltip title={label}>
        <Box sx={{ mb: 0.5 }}>
          {getIcon()}
        </Box>
      </Tooltip>
      <Typography 
        variant="caption" 
        sx={{ 
          color: getColor(),
          fontWeight: (status === 'IN_PROGRESS' || isActive) ? 'bold' : 'normal',
          textAlign: 'center'
        }}
      >
        {label}
      </Typography>
    </Box>
  );
};

interface VerificationStepperProps {
  kycStatus: VerificationStepStatus;
  bankStatus: VerificationStepStatus;
  aiStatus: VerificationStepStatus;
  finalSummaryStatus: VerificationStepStatus;
  activeStep: number;
  onStepClick: (stepIndex: number) => void;
}

const VerificationStepper: FC<VerificationStepperProps> = ({ 
  kycStatus, 
  bankStatus, 
  aiStatus,
  finalSummaryStatus,
  activeStep,
  onStepClick
}) => {
  const theme = useTheme();
  const { t } = useTranslation();

  const Connector = ({ active }: { active: boolean }) => (
    <Box 
      sx={{ 
        flex: '1 1 auto',
        height: 2, 
        bgcolor: active ? theme.palette.success.main : theme.palette.grey[300],
        mt: 1.25,
        mx: 0
      }} 
    />
  );

  return (
    <Box sx={{ display: 'flex', alignItems: 'flex-start', width: '100%', mb: 1 }}>
      <StepCircle 
        status={kycStatus} 
        label={t('client.stepper.kyc')} 
        onClick={() => onStepClick(0)}
        isActive={activeStep === 0}
      />
      <Connector active={kycStatus === 'FINISHED'} />
      <StepCircle 
        status={bankStatus} 
        label={t('client.stepper.bank')} 
        onClick={() => onStepClick(1)}
        isActive={activeStep === 1}
      />
      <Connector active={bankStatus === 'FINISHED'} />
      <StepCircle 
        status={aiStatus} 
        label={t('client.stepper.ai')} 
        onClick={() => onStepClick(2)}
        isActive={activeStep === 2}
      />
      <Connector active={aiStatus === 'FINISHED'} />
      <StepCircle 
        status={finalSummaryStatus} 
        label={t('client.stepper.finalSummary')} 
        onClick={() => onStepClick(3)}
        isActive={activeStep === 3}
      />
    </Box>
  );
};

export default VerificationStepper;
