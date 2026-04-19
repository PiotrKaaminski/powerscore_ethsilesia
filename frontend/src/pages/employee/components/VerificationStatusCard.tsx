import React, { FC } from 'react';
import {
  Paper,
  Typography,
  Box,
  Divider,
  Chip
} from '@mui/material';
import { 
  CheckCircle as CheckCircleIcon,
  Error as ErrorIcon,
  Pending as PendingIcon,
  Help as HelpIcon
} from '@mui/icons-material';
import { useTranslation } from 'react-i18next';
import { VerificationInfo, VerificationStatus } from '../../../types/api';
import EmployeeVerificationStepper from './EmployeeVerificationStepper';

interface VerificationStatusCardProps {
  verification: VerificationInfo;
}

const VerificationStatusCard: FC<VerificationStatusCardProps> = ({ verification }) => {
  const { t } = useTranslation();

  const getStatusIcon = (status: VerificationStatus) => {
    switch (status) {
      case 'VERIFIED':
        return <CheckCircleIcon />;
      case 'FAILED':
        return <ErrorIcon />;
      case 'IN_PROGRESS':
        return <PendingIcon />;
      case 'AWAITING_FINAL_DECISION':
        return <PendingIcon color="info" />;
      default:
        return <HelpIcon />;
    }
  };

  const getStatusColor = (status: VerificationStatus) => {
    switch (status) {
      case 'VERIFIED':
        return 'success';
      case 'FAILED':
        return 'error';
      case 'IN_PROGRESS':
        return 'warning';
      case 'AWAITING_FINAL_DECISION':
        return 'info';
      default:
        return 'default';
    }
  };

  return (
    <Paper elevation={2} sx={{ p: 1, bgcolor: (theme) => theme.palette.mode === 'light' ? 'grey.50' : 'grey.900' }}>
      <Box sx={{ display: 'flex', justifyContent: 'center', mb: 2, mt: 1 }}>
        <Box sx={{ display: 'flex', flexDirection: 'row', alignItems: 'center', gap: 1.5 }}>
          <Chip 
            icon={getStatusIcon(verification.status)}
            label={t(`employee.verificationList.statuses.${verification.status}`)} 
            color={getStatusColor(verification.status)}
            variant="filled"
            sx={{ 
              px: 2, 
              py: 3, 
              fontSize: '1.2rem', 
              fontWeight: 'bold',
              height: 48,
              '& .MuiChip-label': { px: 2 },
              '& .MuiChip-icon': { fontSize: '2rem' }
            }}
          />
        </Box>
      </Box>
      
      <Divider sx={{ my: 1.5 }} />
      
      <Box sx={{ display: 'flex', justifyContent: 'center', py: 1.5, px: 2 }}>
        <Box sx={{ width: '100%' }}>
          <EmployeeVerificationStepper 
            kycStatus={verification.kycVerificationStatus}
            bankStatus={verification.bankVerificationStatus}
            aiStatus={verification.aiRecommendationStatus}
            finalSummaryStatus={verification.finalSummaryStatus}
          />
        </Box>
      </Box>

      <Box sx={{ display: 'flex', justifyContent: 'space-around', flexWrap: 'wrap', gap: 1 }}>
        <Typography variant="caption" color="text.secondary" sx={{ fontSize: '0.7rem' }}>
          <strong>{t('employee.verificationList.startDate')}:</strong> {verification.startDate ? new Date(verification.startDate).toLocaleString() : '-'}
        </Typography>
        <Typography variant="caption" color="text.secondary" sx={{ fontSize: '0.7rem' }}>
          <strong>{t('employee.verificationList.finishDate')}:</strong> {verification.finishDate ? new Date(verification.finishDate).toLocaleString() : '-'}
        </Typography>
      </Box>
    </Paper>
  );
};

export default VerificationStatusCard;
