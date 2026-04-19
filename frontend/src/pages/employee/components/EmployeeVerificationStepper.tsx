import React, { FC } from 'react';
import { 
  Box, 
  Typography, 
  Tooltip,
  useTheme
} from '@mui/material';
import { CheckCircle, FiberManualRecord, RadioButtonUnchecked } from '@mui/icons-material';
import { VerificationStepStatus } from '../../../types/api';
import { useTranslation } from 'react-i18next';

interface StepProps {
  status: VerificationStepStatus;
  label: string;
}

const StepCircle: FC<StepProps> = ({ status, label }) => {
  const theme = useTheme();
  
  const getColor = () => {
    switch (status) {
      case 'FINISHED':
        return theme.palette.success.main;
      case 'IN_PROGRESS':
        return theme.palette.primary.main;
      case 'WAITING_FOR_PREVIOUS':
      default:
        return theme.palette.grey[500];
    }
  };

  const getIcon = () => {
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
      sx={{ 
        display: 'flex', 
        flexDirection: 'column', 
        alignItems: 'center', 
        flex: '1 1 0',
        minWidth: 80
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
          fontWeight: status === 'IN_PROGRESS' ? 'bold' : 'normal',
          textAlign: 'center'
        }}
      >
        {label}
      </Typography>
    </Box>
  );
};

interface EmployeeVerificationStepperProps {
  kycStatus: VerificationStepStatus;
  bankStatus: VerificationStepStatus;
  aiStatus: VerificationStepStatus;
  finalSummaryStatus: VerificationStepStatus;
}

const EmployeeVerificationStepper: FC<EmployeeVerificationStepperProps> = ({ 
  kycStatus, 
  bankStatus, 
  aiStatus,
  finalSummaryStatus
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
      />
      <Connector active={kycStatus === 'FINISHED'} />
      <StepCircle 
        status={bankStatus} 
        label={t('client.stepper.bank')} 
      />
      <Connector active={bankStatus === 'FINISHED'} />
      <StepCircle 
        status={aiStatus} 
        label={t('client.stepper.ai')} 
      />
      <Connector active={aiStatus === 'FINISHED'} />
      <StepCircle 
        status={finalSummaryStatus} 
        label={t('client.stepper.finalSummary')} 
      />
    </Box>
  );
};

export default EmployeeVerificationStepper;
